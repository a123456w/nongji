package com.guo.qlzx.nongji.commen.http;


import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.guo.qlzx.nongji.client.bean.AddBanksBean;
import com.guo.qlzx.nongji.client.bean.AddExpendBean;

import com.guo.qlzx.nongji.client.bean.ArticleListBean;
import com.guo.qlzx.nongji.client.bean.BankDetailsBean;

import com.guo.qlzx.nongji.client.bean.BankListBean;
import com.guo.qlzx.nongji.client.bean.BankNameBean;
import com.guo.qlzx.nongji.client.bean.ConcessionExchangeBean;
import com.guo.qlzx.nongji.client.bean.DeclareRecordListBean;
import com.guo.qlzx.nongji.client.bean.DetailsInfoBean;
import com.guo.qlzx.nongji.client.bean.ExpendBean;
import com.guo.qlzx.nongji.client.bean.FailureDeclareListBean;
import com.guo.qlzx.nongji.client.bean.HomeIndexBean;
import com.guo.qlzx.nongji.client.bean.JobRecordListBean;
import com.guo.qlzx.nongji.client.bean.JournalInfoBean;
import com.guo.qlzx.nongji.client.bean.MachineInfo;
import com.guo.qlzx.nongji.client.bean.MapIndex;
import com.guo.qlzx.nongji.client.bean.MemoryBean;
import com.guo.qlzx.nongji.client.bean.NotewithhandListBean;
import com.guo.qlzx.nongji.client.bean.SerBean;
import com.guo.qlzx.nongji.client.bean.StatisticsBean;
import com.guo.qlzx.nongji.client.bean.WorkIndexBean;
import com.guo.qlzx.nongji.service.bean.ChangepwdBean;
import com.guo.qlzx.nongji.service.bean.CommitAddListBean;
import com.guo.qlzx.nongji.service.bean.DeleteBean;
import com.guo.qlzx.nongji.service.bean.LogBean;
import com.guo.qlzx.nongji.service.bean.LogrecordBean;
import com.guo.qlzx.nongji.service.bean.LogsAsrvicesDetailsBean;
import com.guo.qlzx.nongji.service.bean.LogsSrrvicesBean;
import com.guo.qlzx.nongji.service.bean.MaintainingBean;
import com.guo.qlzx.nongji.service.bean.MaintenanceDetailsBean;
import com.guo.qlzx.nongji.service.bean.ManageMentBean;
import com.guo.qlzx.nongji.service.bean.MessageDetailsBean;
import com.guo.qlzx.nongji.service.bean.MessageListBean;
import com.guo.qlzx.nongji.service.bean.OrderDetailsBean;
import com.guo.qlzx.nongji.service.bean.OrderListBean;
import com.guo.qlzx.nongji.service.bean.OrderManageMentBean;
import com.guo.qlzx.nongji.service.bean.ParsDetailsCommitBean;
import com.guo.qlzx.nongji.service.bean.PartsDetailsBean;
import com.guo.qlzx.nongji.service.bean.PartyLogBean;
import com.guo.qlzx.nongji.service.bean.PartyLoginBean;
import com.guo.qlzx.nongji.service.bean.RechargeRecordListBean;
import com.guo.qlzx.nongji.service.bean.RegBean;
import com.guo.qlzx.nongji.service.bean.ReplaceListBean;
import com.guo.qlzx.nongji.service.bean.logOutBean;
import com.qlzx.mylibrary.bean.BaseBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import rx.Observable;

public interface RemoteApi {


    /**
     * 合计作业统计
     *
     * @param token
     * @param start
     * @param end
     * @return
     */
    @POST("Journal/statistics")
    @FormUrlEncoded
    Observable<BaseBean<StatisticsBean>> statistics(@Field("token") String token,
                                                    @Field("start") String start,
                                                    @Field("end") String end
    );
    /*
    * 文章列表接口
    * */
    @POST("Article/articleList")
    @FormUrlEncoded
    Observable<BaseBean<List<ArticleListBean>>> articleList(@Field("type") String type,
                                                            @Field("page") int page );

    @POST("Home/homeIndex")
    @FormUrlEncoded
    Observable<BaseBean<HomeIndexBean>> homeIndex(@Field("token") String token
    );

    /**
     * 日志详情页 获取整个时间段
     *
     * @param token
     */
    @POST("Journal/detailsInfo")
    @FormUrlEncoded
    Observable<BaseBean<DetailsInfoBean>> detailsInfo(@Field("token") String token,
                                                      @Field("machine_id") String machine_id,
                                                      @Field("time") String time
    );

    /**
     * 日志详情页  获取单个时间段数据
     *
     * @param token
     * @param sn
     * @param start
     * @param end
     * @return
     */
    @POST("Journal/journalInfo")
    @FormUrlEncoded
    Observable<BaseBean<JournalInfoBean>> journalInfo(@Field("token") String token,
                                                      @Field("machine_id") String machine_id,
                                                      @Field("start") String start,
                                                      @Field("end") String end
    );


    /**
     * 首页数据统计内容
     *
     * @param token
     * @return
     */
    @POST("Home/workIndex")
    @FormUrlEncoded
    Observable<BaseBean<WorkIndexBean>> workIndex(@Field("token") String token
    );

    /**
     * 机器详情
     *
     * @param token
     * @param sn
     * @return
     */
    @POST("Home/machineInfo")
    @FormUrlEncoded
    Observable<BaseBean<MachineInfo>> machineInfo(@Field("token") String token
            , @Field("sn") String sn
    );

    /**
     * 首页地图内容
     *
     * @param token
     * @return
     */
    @POST("Home/mapIndex")
    @FormUrlEncoded
    Observable<BaseBean<List<MapIndex>>> mapIndex(@Field("token") String token
    );

    /*    *//**
     * 支付  微信/支付宝
     *
     * @param token
     * @param paytype
     * @param money
     * @param sid
     * @return
     *//*
    @POST("Pay/pay_order")
    @FormUrlEncoded
    Observable<BaseBean<PayBean>> payOrder(@Field("token") String token,
                                           @Field("paytype") String paytype,
                                           @Field("money") String money,
                                           @Field("sid") String sid);*/

    /**
     * 获取验证码
     *
     * @param mobile
     * @param type   1注册验证码,2登录验证码,3找回密码验证码,4添加银行卡认证验证码,5绑定手机号验证码
     * @return
     */
    @POST("SendMessage/send")
    @FormUrlEncoded
    Observable<BaseBean<String>> getCode(@Field("mobile") String mobile,
                                         @Field("type") int type);

    /**
     * 登录
     *
     * @param mobile
     * @param password
     * @param registration_id
     * @return
     */
    @POST("LoginUser/login")
    @FormUrlEncoded
    Observable<BaseBean<LogBean>> Login(@Field("mobile") String mobile,
                                        @Field("password") String password,
                                        @Field("registration_id") String registration_id
    );

    /**
     * 注册
     *
     * @param mobile
     * @param password
     * @param code
     * @return
     */
    @POST("LoginUser/register")
    @FormUrlEncoded
    Observable<BaseBean<RegBean>> register(@Field("mobile") String mobile,
                                           @Field("password") String password,
                                           @Field("code") String code,
                                           @Field("registration_id") String registration_id);
    //@param registration_id

    /**
     * 修改密码
     *
     * @param token
     * @param old_password
     * @param new_password
     * @param new_password2
     * @return
     */
    @POST("Login/updatePassword")
    @FormUrlEncoded
    Observable<BaseBean<ChangepwdBean>> changepassword(@Field("token") String token,
                                                       @Field("old_password") String old_password,
                                                       @Field("new_password") String new_password,
                                                       @Field("new_password2") String new_password2);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    @POST("Login/exitLogin")
    @FormUrlEncoded
    Observable<BaseBean<logOutBean>> loginout(@Field("token") String token);


    /**
     * 优惠券兑换
     *
     * @param token
     * @param coupon
     * @return
     */
    @POST("Coupon/exchangeCoupon")
    @FormUrlEncoded
    Observable<BaseBean<ConcessionExchangeBean>> getconcession(@Field("token") String token,
                                                               @Field("coupon") String coupon
    );

    /**
     * 管理系统
     *
     * @param token
     * @return
     */
    @POST("User/manageSystem")
    @FormUrlEncoded
    Observable<BaseBean<List<ManageMentBean>>> getment(@Field("token") String token,
    @Field("page") String page);

    /**
     * 管理系统详情列表
     *
     * @param token
     * @param id
     * @return
     */
    @POST("User/orderSystem")
    @FormUrlEncoded
    Observable<BaseBean<List<OrderManageMentBean>>> getorder(@Field("token") String token,
                                                             @Field("id") String id);

    /**
     * 管理系统详情列表删除
     *
     * @param token
     * @param id
     * @return
     */
    @POST("User/deleteMachine")
    @FormUrlEncoded
    Observable<BaseBean<DeleteBean>> getdele(@Field("token") String token,
                                             @Field("id") int id);

    /**
     * 管理系统详情列表替换机器列表
     *
     * @param token
     * @param machine_id
     * @return
     */
    @POST("User/getMachineList")
    @FormUrlEncoded
    Observable<BaseBean<List<ReplaceListBean>>> getreplace(@Field("token") String token,
                                                           @Field("machine_id") int machine_id);

    /**
     * 管理系统详情列表确定替换
     *
     * @param token
     * @param old_id
     * @param new_id
     * @param operator_id
     * @param order_id
     * @return
     */
    @POST("User/replaceMachine")
    @FormUrlEncoded
    Observable<BaseBean<Object>> getreplacement(@Field("token") String token,
                                                @Field("old_id") String old_id,
                                                @Field("new_id") String new_id,
                                                @Field("operator_id") String operator_id,
                                                @Field("order_id") String order_id);

    /**
     * 管理系统详情列表添加列表
     *
     * @return token    是	string	用户token
     * order_id	是	int	订单的id
     */
    @POST("User/machineList")
    @FormUrlEncoded
    Observable<BaseBean<List<CommitAddListBean>>> getaddlist(@Field("token") String token,
                                                             @Field("order_id") String order_id
    );

    /**
     * 管理系统详情列表添加确认
     *
     * @param token
     * @param old_id
     * @param machine_id
     * @param mobile
     * @return
     */
    @POST("User/replaceOperator")
    @FormUrlEncoded
    Observable<BaseBean> getcommit(@Field("token") String token,
                                   @Field("order_id") String old_id,
                                   @Field("machine_id") String machine_id,
                                   @Field("mobile") String mobile);

    /**
     * 消息列表
     *
     * @param token
     * @param page
     * @return
     */
    @POST("IndexNotice/noticeList")
    @FormUrlEncoded
    Observable<BaseBean<List<MessageListBean>>> getMessageData(@Field("token") String token,
                                                               @Field("page") int page);
    /**
     * 是否有未读消息
     *
     * @param token
     * @param page
     * @return
     */
    @POST("IndexNotice/isHaveRedDot")
    @FormUrlEncoded
    Observable<BaseBean<String>> isHaveRedDot(@Field("token") String token);
    /**
     * 消息详情
     *
     * @param token
     * @param id
     * @return
     */
    @POST("IndexNotice/noticeInfo")
    @FormUrlEncoded
    Observable<BaseBean<MessageDetailsBean>> getMessageDetails(@Field("token") String token,
                                                               @Field("id") String id);

    /**
     * 删除消息
     *
     * @param token
     * @param id
     * @return
     */
    @POST("IndexNotice/deleteNotice")
    @FormUrlEncoded
    Observable<BaseBean> deleteMessage(@Field("token") String token,
                                       @Field("id") String id);

    /**
     * 获取订单列表
     *
     * @param token
     * @param id
     * @param type
     * @param title
     * @return
     */
    @POST("Order/orderList")
    @FormUrlEncoded
    Observable<BaseBean<List<OrderListBean>>> getOrderList(@Field("token") String token,
                                                           @Field("page") int id,
                                                           @Field("type") int type,
                                                           @Field("title") String title);

    /**
     * 订单详情界面
     *
     * @param token
     * @param id
     * @return
     */
    @POST("Order/orderInfo")
    @FormUrlEncoded
    Observable<BaseBean<OrderDetailsBean>> getOrderDetailsData(@Field("token") String token,
                                                               @Field("id") String id);

    /**
     * 修改支付密码
     *
     * @param token
     * @param old_password
     * @param new_password
     * @param new_password2
     * @return
     */
    @POST("Wallet/updatePayPassword")
    @FormUrlEncoded
    Observable<BaseBean> changePayPw(@Field("token") String token,
                                     @Field("old_password") String old_password,
                                     @Field("new_password") String new_password,
                                     @Field("new_password2") String new_password2);

    /**
     * 故障记录
     *
     * @param token
     * @param page
     * @return
     */
    @POST("Repair/repairMessageList")
    @FormUrlEncoded
    Observable<BaseBean<List<DeclareRecordListBean>>> getDeclareRecordData(@Field("token") String token,
                                                                           @Field("page") int page);

    /**
     * 故障申报第一步：获取机器信息
     *
     * @param token
     * @return
     */
    @POST("Repair/report")
    @FormUrlEncoded
    Observable<BaseBean<List<FailureDeclareListBean>>> getFailureDeclareListData(@Field("token") String token);

    /**
     * 故障申报第二步：提交
     *
     * @param body
     * @return
     */
    @POST("Repair/submit")
    Observable<BaseBean> submitFailureDeclareListData(@Body RequestBody body);

    /**
     * 所有的H5请求
     *
     * @param type
     * @return
     */
    @POST("Article/article")
    @FormUrlEncoded
    Observable<BaseBean<String>> getHtml(@Field("type") String type);

    /**
     * 注销账号取消状态
     *
     * @param token
     * @return
     */
    @POST("Personal/checkCancel")
    @FormUrlEncoded
    Observable<BaseBean> getcancel(@Field("token") String token);

    /**
     * 注销账号确定
     *
     * @param token
     * @return
     */
    @POST("Personal/cancel")
    @FormUrlEncoded
    Observable<BaseBean> getrevocation(@Field("token") String token);

    /**
     * 注销账号取消
     *
     * @param token
     * @return
     */
    @POST("Personal/noCancel")
    @FormUrlEncoded
    Observable<BaseBean> getnorevocation(@Field("token") String token);

    /**
     * 保养列表
     *
     * @param token
     * @return
     * @page page
     */
    @POST("Maintain/maintainList")
    @FormUrlEncoded
    Observable<BaseBean<List<MaintainingBean>>> getmaintain(@Field("token") String token,
                                                            @Field("page") int page);

    /**
     * 机器保养详情
     *
     * @param token
     * @return
     * @machine_id machine_id
     */
    @POST("Maintain/maintainInfo")
    @FormUrlEncoded
    Observable<BaseBean<List<MaintenanceDetailsBean>>> getmaintaininfo(@Field("token") String token,
                                                                       @Field("machine_id") String machine_id);

    /**
     * 检查是否设置了支付密码
     *
     * @param token
     * @return
     */
    @POST("Wallet/checkPayPassword")
    @FormUrlEncoded
    Observable<BaseBean> isSetPayPw(@Field("token") String token);

    /**
     * 设置支付密码
     *
     * @param token
     * @param password
     * @param password2
     * @return
     */
    @POST("Wallet/setPayPassword")
    @FormUrlEncoded
    Observable<BaseBean> setPayPw(@Field("token") String token,
                                  @Field("password") String password,
                                  @Field("password2") String password2);

    /**
     * 钱包 获取余额
     *
     * @param token
     * @return
     */
    @POST("Wallet/walletInfo")
    @FormUrlEncoded
    Observable<BaseBean<MemoryBean>> getMemoryData(@Field("token") String token);

    /**
     * 钱包 -提现
     *
     * @param token
     * @param bank_id
     * @param money
     * @param pay_password
     * @return
     */
    @POST("Wallet/applyForward")
    @FormUrlEncoded
    Observable<BaseBean> getMemoryCash(@Field("token") String token,
                                       @Field("bank_id") String bank_id,
                                       @Field("money") String money,
                                       @Field("pay_password") String pay_password);


    /**
     * 银行卡-获取银行卡列表
     *
     * @param token
     * @return
     */
    @POST("BankCard/bankCardList")
    @FormUrlEncoded
    Observable<BaseBean<List<BankListBean>>> getBankList(@Field("token") String token);

    /**
     * 机器零件保养详情
     *
     * @param token
     * @param position_id
     * @return
     * @machine_id machine_id
     */
    @POST("Maintain/showMaintain")
    @FormUrlEncoded
    Observable<BaseBean<PartsDetailsBean>> getshow(@Field("token") String token,
                                                   @Field("machine_id") String machine_id,
                                                   @Field("position_id") String position_id
    );

    /**
     * 日志
     *
     * @param token
     * @return
     */
    @POST("Journal/journalList")
    @FormUrlEncoded
    Observable<BaseBean<List<LogrecordBean>>> getjournal(@Field("token") String token);

    /**
     * 机器零件保养详情提交
     *
     * @param token
     * @param position_id
     * @param work_time
     * @return
     * @machine_id machine_id
     */
    @POST("Maintain/doMaintain")
    @FormUrlEncoded
    Observable<BaseBean<ParsDetailsCommitBean>> getdomaintain(@Field("token") String token,
                                                              @Field("machine_id") String machine_id,
                                                              @Field("position_id") String position_id,
                                                              @Field("work_time") int work_time
    );

    /**
     * 意见反馈
     */
    @POST("Opinion/submit")
    Observable<BaseBean<BaseBean>> getSubmit(@Body RequestBody body);

    /**
     * 客户端钱包-随手记列表
     *
     * @param token
     * @param start
     * @param end
     * @return
     */
    @POST("Note/noteList")
    @FormUrlEncoded
    Observable<BaseBean<NotewithhandListBean>> getnotelist(@Field("token") String token,
                                                           @Field("start") long start,
                                                           @Field("end") long end
    );


    /**
     * 订单- 充值列表
     *
     * @param token
     * @param page
     * @return
     */
    @POST("Bill/billList")
    @FormUrlEncoded
    Observable<BaseBean<List<RechargeRecordListBean>>> getOrderRecharge(@Field("token") String token, @Field("page") int page, @Field("order_id") String order_id);

    /**
     * 钱包-交易列表
     *
     * @param token
     * @param page
     * @return
     */
    @POST("Wallet/forwardList")
    @FormUrlEncoded
    Observable<BaseBean<List<RechargeRecordListBean>>> getForwardList(@Field("token") String token, @Field("page") int page);


    /**
     * 添加银行卡 第一步 - 获取用户名
     *
     * @param token
     * @return
     */
    @POST("BankCard/getUserName")
    @FormUrlEncoded
    Observable<BaseBean<AddBanksBean>> getUserName(@Field("token") String token);

    /**
     * 添加银行卡 第二步 - 获取银行卡类型
     *
     * @param token
     * @param bank_number
     * @param pay_password
     * @return
     */
    @POST("BankCard/getBankType")
    @FormUrlEncoded
    Observable<BaseBean<BankNameBean>> getBankType(@Field("token") String token, @Field("bank_number") String bank_number, @Field("pay_password") String pay_password);
    /**
     * 添加银行卡 第二步 - 获取银行卡类型
     *
     * @param token
     * @param bank_number
     * @param pay_password
     * @return
     */
    @POST("BankCard/getBankType")
    @FormUrlEncoded
    Observable<BaseBean<BankNameBean>> getBankTypeIdentity(@Field("token") String token, @Field("bank_number") String bank_number, @Field("pay_password") String pay_password,
    @Field("name") String name, @Field("id_number") String id_number);

    /**
     * 随手记支出获取标签
     *
     * @param token
     * @param type
     * @return
     */
    @POST("Note/getLabel")
    @FormUrlEncoded
    Observable<BaseBean<List<ExpendBean>>> getLabel(@Field("token") String token,
                                                    @Field("type") int type);

    /**
     * 客服
     *
     * @return
     */
    @POST("ServiceTelephone/telephoneList")
    Observable<BaseBean<List<SerBean>>> gettel();

    /**
     * 添加银行卡 第三步 - 完成类型
     *
     * @param token
     * @param bank_number
     * @param mobile
     * @param code
     * @return
     */
    @POST("BankCard/addBankCard")
    @FormUrlEncoded
    Observable<BaseBean> finishBankCard(@Field("token") String token, @Field("mobile") String mobile, @Field("code") String code ,@Field("bank_number") String bank_number);

    /**
     * 获取银行卡详情
     *
     * @param token
     * @param id
     * @return
     */
    @POST("BankCard/bankCardInfo")
    @FormUrlEncoded
    Observable<BaseBean<BankDetailsBean>> getBankDetailsData(@Field("token") String token, @Field("id") String id);

    /**
     * 解绑
     *
     * @param token
     * @param id
     * @param pay_password
     * @return
     */
    @POST("BankCard/untieBankCard")
    @FormUrlEncoded
    Observable<BaseBean> unBindBankCard(@Field("token") String token, @Field("id") String id, @Field("pay_password") String pay_password);

    /**
     * 个人中心-作业记录
     *
     * @param token
     * @return
     */
    @POST("Personal/workRecord")
    @FormUrlEncoded
    Observable<BaseBean<List<JobRecordListBean>>> getJobRecordData(@Field("token") String token);

    /**
     * 随手记支出添加标签
     *
     * @param token
     * @param type
     * @param label_name
     * @return
     */
    @POST("Note/addLabel")
    @FormUrlEncoded
    Observable<BaseBean<List<AddExpendBean>>> getAddLabel(@Field("token") String token,
                                                          @Field("type") int type,
                                                          @Field("label_name") String label_name);

    /**
     * 随手记提交
     *
     * @param token
     * @param label_id
     * @param money
     * @param create_time
     * @param type
     * @param content
     * @return
     */
    @POST("Note/addRecord")
    @FormUrlEncoded
    Observable<BaseBean> getRecord(@Field("token") String token,
                                   @Field("label_id") String label_id,
                                   @Field("create_time") long create_time,
                                   @Field("money") String money,
                                   @Field("type") int type,
                                   @Field("content") String content)


    ;

    /**
     * 服务日志列表
     *
     * @param token
     * @return
     */
    @POST("RepairDeal/serviceJournalList")
    @FormUrlEncoded
    Observable<BaseBean<List<LogsSrrvicesBean>>> getServiceJour(@Field("token") String token
    );

    /**
     * 服务日志列表正常
     *
     * @param token
     * @param id
     * @return
     */
    @POST("RepairDeal/normal")
    @FormUrlEncoded
    Observable<BaseBean> getNormal(@Field("token") String token,
                                   @Field("id") String id
    );

    /**
     * 服务日志列表报修处理
     *
     * @param token
     * @param journal_id
     * @return
     */
    @POST("RepairDeal/showRepairDeal")
    @FormUrlEncoded
    Observable<BaseBean<List<LogsAsrvicesDetailsBean>>> getShowRepair(@Field("token") String token,
                                                                      @Field("journal_id") String journal_id
    );

    /**
     * 服务日志列表报修处理第一次提交
     *
     * @param token
     * @param journal_id
     * @param status
     * @param reason_id
     * @param pic
     * @param content
     * @param score
     * @return
     */
    @POST("RepairDeal/firstSubmit")
    Observable<BaseBean> getFirstSubmit(@Body RequestBody body);

    /**
     * 服务日志列表报修处理第二次提交
     *
     * @param token
     * @param journal_id
     * @param detailed
     * @param fault_pic
     * @param solve_pic
     * @param enclosure
     * @return
     */
    @POST("RepairDeal/secondSubmit")
    Observable<BaseBean> getSecondSubmit(@Body RequestBody body
    );

    /**
     * 登录忘记密码
     *
     * @param mobile
     * @param password
     * @param code
     * @return
     */
    @POST("LoginUser/forgetPassword")
    @FormUrlEncoded
    Observable<BaseBean> getForgetPassword(@Field("mobile") String mobile,
                                           @Field("password") String password,
                                           @Field("code") String code
    );

    /**
     * 第三方登录
     *
     * @param openid
     * @param type
     * @return
     */
    @POST("LoginUser/qqLogin")
    @FormUrlEncoded
    Observable<BaseBean<PartyLogBean>> getLoginPar(@Field("openid") String openid,
                                                   @Field("type") int type

    );

    /**
     * 第三方登录绑定设置密码
     *
     * @param openid
     * @param type
     * @param mobile
     * @param password
     * @param code
     * @param registration_id
     * @return
     */
    @POST("LoginUser/bindingMobile")
    @FormUrlEncoded
    Observable<BaseBean<PartyLoginBean>> getBindingMobile(@Field("openid") String openid,
                                                          @Field("type") String type,
                                                          @Field("mobile") String mobile,
                                                          @Field("password") String password,
                                                          @Field("code") String code,
                                                          @Field("registration_id") String registration_id
    );
}