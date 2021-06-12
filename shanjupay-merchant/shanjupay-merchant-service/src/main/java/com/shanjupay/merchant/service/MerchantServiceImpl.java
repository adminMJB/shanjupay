package com.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.PhoneUtil;
import com.shanjupay.common.util.StringUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.convert.MerchantCovert;
import com.shanjupay.merchant.convert.StaffConvert;
import com.shanjupay.merchant.convert.StoreConvert;
import com.shanjupay.merchant.dto.MerchantDTO;
import com.shanjupay.merchant.dto.StaffDTO;
import com.shanjupay.merchant.dto.StoreDTO;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.entity.Staff;
import com.shanjupay.merchant.entity.Store;
import com.shanjupay.merchant.entity.StoreStaff;
import com.shanjupay.merchant.mapper.MerchantMapper;
import com.shanjupay.merchant.mapper.StaffMapper;
import com.shanjupay.merchant.mapper.StoreMapper;
import com.shanjupay.merchant.mapper.StoreStaffMapper;
import com.shanjupay.user.api.TenantService;
import com.shanjupay.user.api.dto.tenant.CreateTenantRequestDTO;
import com.shanjupay.user.api.dto.tenant.TenantDTO;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 描述
 *
 * @author 马佳彬
 * @version 1.0
 * @date 2021/5/16 15:24
 **/
@Service
public class MerchantServiceImpl implements MerchantService {
    /**
     * 注入merchantMapper
     */
    @Autowired
    private MerchantMapper merchantMapper;
    /**
     * 门店mapper
     */
    @Autowired
    private StoreMapper storeMapper;
    /**
     * 员工mapper
     */
    @Autowired
    private StaffMapper staffMapper;
    /**
     * 光和门店关系mapper
     */
    @Autowired
    private StoreStaffMapper storeStaffMapper;

    /**
     * 注入SaaS商户服务
     */
    @Reference
    private TenantService tenantService;

    /**
     * 通过id查询商品
     *
     * @param id 商品id
     * @return MerchantDTO
     */
    @Override
    public MerchantDTO queryMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);
        return MerchantCovert.INSTANCE.entry2dto(merchant);
    }

    /**
     * 商户注册
     *
     * @param merchantDTO 商户注册信息
     * @return
     */
    @Override
    public MerchantDTO creatMerchant(MerchantDTO merchantDTO) throws BusinessException {
        if (null == merchantDTO) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtil.isEmpty(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        if (!PhoneUtil.isMatches(merchantDTO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        if (StringUtil.isEmpty(merchantDTO.getPassword())) {
            throw new BusinessException(CommonErrorCode.E_100111);
        }
        //校验手机号唯一性
        int count = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>().eq(Merchant::getMobile, merchantDTO.getMobile()));
        if (count > 0) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        CreateTenantRequestDTO createTenantRequestDTO = new CreateTenantRequestDTO();
        createTenantRequestDTO.setMobile(merchantDTO.getMobile());
        createTenantRequestDTO.setUsername(merchantDTO.getUsername());
        createTenantRequestDTO.setPassword(merchantDTO.getPassword());
        createTenantRequestDTO.setTenantTypeCode("shanju-merchant");
        createTenantRequestDTO.setBundleCode("shanju-merchant");
        createTenantRequestDTO.setName(merchantDTO.getUsername());
        TenantDTO tenantAndAccount = tenantService.createTenantAndAccount(createTenantRequestDTO);
        if (tenantAndAccount == null || tenantAndAccount.getId() == null) {
            throw new BusinessException(CommonErrorCode.E_200012);
        }
        Long tenantAndAccountId = tenantAndAccount.getId();
        Integer selectCount = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>().eq(Merchant::getTenantId, tenantAndAccountId));
        if (selectCount > 0) {
            throw new BusinessException(CommonErrorCode.E_200017);
        }

        //对象转换
        Merchant merchant = MerchantCovert.INSTANCE.dto2entry(merchantDTO);
        //设置状态-申请
        merchant.setAuditStatus("0");
        merchant.setTenantId(tenantAndAccountId);
        merchantMapper.insert(merchant);

        //新增门店
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreName("根门店");
        storeDTO.setMerchantId(merchant.getId());
        StoreDTO store = createStore(storeDTO);

        //新增员工
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setMobile(merchant.getMobile());
        staffDTO.setUsername(merchant.getUsername());
        staffDTO.setStoreId(store.getId());
        staffDTO.setMerchantId(merchant.getId());
        staffDTO.setStaffStatus(true);
        StaffDTO staff = createStaff(staffDTO);


        //绑定关系
        bindStaffToStore(store.getId(), staff.getId());


        return merchantDTO;
    }

    /**
     * 资质申请接口
     *
     * @param merchantId  id
     * @param merchantDTO 请求参数
     * @throws BusinessException
     */
    @Override
    public void applyMerchant(Long merchantId, MerchantDTO merchantDTO) throws BusinessException {
        if (null == merchantId || null == merchantDTO) {
            throw new BusinessException(CommonErrorCode.E_100101);
        }
        //校验merchantId合法性，查询商户表，如果查询不到记录，认为非法
        Merchant merchant = MerchantCovert.INSTANCE.dto2entry(merchantDTO);
        Merchant entity = merchantMapper.selectById(merchant.getId());
        if (null == entity) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        merchant.setId(merchant.getId());
        //因为资质申请的时候手机号不让改，还使用数据库中原来的手机号
        merchant.setMobile(merchant.getMobile());
        //审核状态2-已申请待审核
        merchant.setAuditStatus("2");
        merchant.setTenantId(merchant.getTenantId());
        //保存到数据库
        LambdaUpdateWrapper<Merchant> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Merchant::getId, merchant.getId());
        merchantMapper.update(merchant, lambdaUpdateWrapper);
    }

    /**
     * 新增门店
     *
     * @param storeDTO
     * @return
     * @throws BusinessException
     */
    @Override
    public StoreDTO createStore(StoreDTO storeDTO) throws BusinessException {
        //对象转换
        Store store = StoreConvert.INSTANCE.dto2entity(storeDTO);
        storeMapper.insert(store);
        return StoreConvert.INSTANCE.entity2dto(store);
    }

    /**
     * 新增员工
     *
     * @param staffDTO
     * @return
     */
    @Override
    public StaffDTO createStaff(StaffDTO staffDTO) {
        //校验参数
        if (staffDTO == null || StringUtil.isEmpty(staffDTO.getMobile())
                || StringUtil.isEmpty(staffDTO.getUsername()) || staffDTO.getStoreId() == null) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        if (isExistStaffByMobile(staffDTO.getMobile(), staffDTO.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100113);
        }
        if (isExistStaffByUserName(staffDTO.getUsername(), staffDTO.getMerchantId())) {
            throw new BusinessException(CommonErrorCode.E_100114);
        }

        //对象转换
        Staff staff = StaffConvert.INSTANCE.dto2entity(staffDTO);
        staffMapper.insert(staff);
        return StaffConvert.INSTANCE.entity2dto(staff);
    }

    /**
     * 为门店设置管理员
     *
     * @param storeId
     * @param staffId
     * @throws BusinessException
     */
    @Override
    public void bindStaffToStore(Long storeId, Long staffId) throws BusinessException {
        StoreStaff storeStaff = new StoreStaff();
        storeStaff.setStoreId(storeId);
        storeStaff.setStaffId(staffId);
        storeStaffMapper.insert(storeStaff);
    }

    @Override
    public MerchantDTO queryMerchantByTenantId(Long tenantId) {
        Merchant merchant = merchantMapper.selectOne(new QueryWrapper<Merchant>().lambda().eq(Merchant::getTenantId, tenantId));
        return MerchantCovert.INSTANCE.entry2dto(merchant);
    }

    /**
     * 检验手机号是否存在
     *
     * @param mobile
     * @param merchantId
     * @return
     */
    private Boolean isExistStaffByMobile(String mobile, Long merchantId) {
        LambdaQueryWrapper<Staff> lambdaQueryWrapper = new LambdaQueryWrapper<Staff>();
        lambdaQueryWrapper.eq(Staff::getMobile, mobile).eq(Staff::getMerchantId, merchantId);
        int i = staffMapper.selectCount(lambdaQueryWrapper);
        return i > 0;
    }

    /**
     * 校验装好是否存在
     *
     * @param userName
     * @param merchantId
     * @return
     */
    private Boolean isExistStaffByUserName(String userName, Long merchantId) {
        LambdaQueryWrapper<Staff> lambdaQueryWrapper = new LambdaQueryWrapper<Staff>();
        lambdaQueryWrapper.eq(Staff::getUsername, userName).eq(Staff::getMerchantId, merchantId);
        int i = staffMapper.selectCount(lambdaQueryWrapper);
        return i > 0;
    }
}
