package com.example.alarmclock;

public class Config {
	public static final String URL = "http://www.shandkj.com/";
	public static final String UPDATE_CODE="apk/updatelld.json";
	public static final String SHARELOGO_CODE=URL+"page/images/lld.png";
	public static final String REGISTERADDRES_CODE=URL+"wx/main/lldzcxy.html";
	public static final String OUTMONEYPROTOL_CODE=URL+"wx/main/lldjkxy.html";
	public static final String BANKCARDPROTOL_CODE=URL+"wx/main/lldrzxy.html";
	public static final String PROLEM_CODE=URL+"androidHtml/lldcjwt_app.html";
	//登录接口
	public static final String LOGIN_CODE="servlet/current/JBDUserAction?function=LoginE2YN";
	//注册接口
	public static final String REGISTER = "servlet/current/JBDUserAction?function=RegisterNewYN";
	//注册获取短信验证码
	public static final String SEND_CODE = "servlet/current/JBDUserAction?function=SDSendMsgNewYN";
	public static final String FORGET_CODE="servlet/current/JBDUserAction?function=FindPasswordGS";
	public static final String FORGET_SEND_CODE="servlet/current/JBDUserAction?function=LLDSendMsgNewFindPwd";
	public static final String CHECK_CODE="servlet/current/JBDUserAction?function=FindPassword";
	//
	public static final String HOME_INIT=URL+"servlet/current/JBDUserAction?function=FindUsernameAppYN";
	//
	public static final String GETBANKCARD=URL+"servlet/current/JBDUserAction?function=GetBankCard";
	public static final String OUTMONEY_RECORD=URL+"servlet/current/JBDUserAction?function=GetShowJD";
	public static final String PAY_RESULT_CORD=URL+"servlet/current/JBDModelAction?function=getAuthResultApp";
	public static final String PAY_START_CORD=URL+"servlet/current/JBDModelAction?function=AppPayAuth";
	public static final String RELATION_INFO_CORD=URL+"servlet/current/JBDUserAction?function=AddContactIofo";
	public static final String WORK_INFO_CORD=URL+"servlet/current/JBDUserAction?function=AddJobIofo";
	public static final String CARD_BANAME_CORD=URL+"servlet/current/JBDUserAction";
	public static final String IWANTMONEY_CORD=URL+"servlet/current/JBDUserAction?function=AddUserJK";
	public static final String AUTHMOBILE_CORD=URL+"servlet/current/JBDUserAction?function=GetConversation";
	public static final String AUTHTAOBAO_CORD=URL+"servlet/current/JBDUserAction?function=GetTabao";
	public static final String AUTHJD_CORD=URL+"servlet/current/JBDUserAction?function=GetJD";
	public static final String JKSEND_CORD=URL+"servlet/current/JBDUserAction?function=SendGSDJkMsg";
	public static final String JKRECORD_CORD=URL+"servlet/current/JBDUserAction?function=ShowJKJD&type=0";
	public static final String MONEYRECORDE_CORD=URL+"servlet/current/JBDUserAction?function=ShowTGInfo";
	public static final String VIDEO_CORD=URL+"servlet/current/JBDUserAction?function=GetSPSH";
	public static final String SHARE_CORD=URL+"LLD/lld.html?";
	public static final String BACKMONEYINIT_CORD=URL+"servlet/current/JBDUserAction?function=RepaymentJK";
	public static final String BACKMONEY_CORD=URL+"servlet/current/JBDModelAction?function=RepaymentJKMoney";
	public static final String ASKMONEYINIT_CORD=URL+"servlet/current/JBDUserAction?function=GetYHKT";
	public static final String ASKMONEY_CORD=URL+"servlet/current/JBDUserAction?function=TjJKMoney";
	public static final String NEWS_CORD=URL+"servlet/current/JBDUserAction?function=FindMsg";
	public static final String COMMITINVESTER_CORD = URL
			+ "servlet/current/JBDInvestAction?function=AddInvestor";
	public static final String BORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=GetInvestList";
	public static final String BORROWPERSONDETAIL_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ToInvest";
	public static final String CHECKBORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=SubmitInvestment";
	public static final String CANCLEBORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=InvestmentReturn";
	public static final String PAYBORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=InvestmentResults";
	public static final String INVESTMONEY_CORD = URL
			+ "servlet/current/JBDInvestAction?function=InvestmentInformation";
	public static final String INVESTMONEYRECORDE_CORD = URL
			+ "servlet/current/JBDInvestAction?function=onloan";
	public static final String OVERDUELIST_CORD = URL
			+ "servlet/current/JBDInvestAction?function=OverdueList";
	public static final String OVERDUEPHONE_CORD = URL
			+ "servlet/current/JBDInvestAction?function=Tocollect";
	public static final String TXMONEY_CORD = URL
			+ "servlet/current/JBDUserAction?function=PresentationRecord";
	public static final String PAY_RESULTLL_CORD = URL
			+ "servlet/current/JBDLLpayAction?function=GetAuthResultApp";//连连支付
	public static final String PAY_LLSTART_CORD = URL
			+ "servlet/current/JBDLLpayAction?function=AppPayAuth";//连连支付
	public static final String BACKMONEYLL_CORD = URL
			+ "servlet/current/JBDLLpayAction?function=RepaymentJKMoney";//连连支付
	public static final String CONFIRMBM_CORD = URL
			+ "servlet/current/JBDcms3Action?function=Repayment";
	
	//
	public static final String GETSHENFEN = URL
			+ "servlet/current/JBDUserAction?function=GetShenfen";
	//
	public static final String GETWORK = URL
			+ "servlet/current/JBDUserAction?function=GetWork";
	//
	public static final String GETSCHOOL = URL
			+ "servlet/current/JBDUserAction?function=GetSchool";
	//
	public static final String GETCONTACT = URL
			+ "servlet/current/JBDUserAction?function=GetContact";
	
	
	
	public static final int CODE_UPDATE_DIALOG = 0;
	public static final int CODE_URL_ERROR = 1;
	public static final int CODE_NET_ERROR = 2;
	public static final int CODE_JSON_ERROR = 3;
	public static final int CODE_ENTER_HOME = 4;
	public static final int CODE_LOGIN = 1001;
	public static final int CODE_SEND = 1002; 
	public static final int CODE_RGISTER = 1003;
	public static final int CODE_FORGET_PWD = 1004;
	public static final int CODE_FORGET_SEND_PWD = 1005;
	public static final int CODE_CHECk_SEND_PWD = 1006;
	public static final int CODE_HOME_INIT = 1007;
	public static final int CODE_OUTMONEY_INIT = 1008;
	public static final int CODE_PAY_RESULT= 1009;
	public static final int CODE_PAY_START= 1010;
	public static final int CODE_RELATION_INFO= 1011;
	public static final int CODE_WORK_INFO= 1012;
	public static final int CODE_CARD_MANAGE= 1013;
	public static final int CODE_IWANTMONEY= 1014;
	public static final int CODE_AUTHMOBILE= 1015;
	public static final int CODE_JKSEND= 1016;
	public static final int CODE_JKRECORD= 1017;
	public static final int CODE_MONEYRECORDE= 1018;
	public static final int CODE_VIDEO= 1019;
	public static final int CODE_SHARE= 1020;
	public static final int CODE_BACKMONEY= 1021;
	public static final int CODE_BACKMONEYINIT= 1022;
	public static final int CODE_ASKMONEYINIT= 1023;
	public static final int CODE_ASKMONEY= 1024;
	public static final int CODE_NEWS= 1025;
	public static final int CODE_BANK= 1026;
	public static final int CODE_ZMSTART= 1027;
	public static final int CODE_ZMEND= 1028;
	public static final int CODE_TXMONEY = 1029;
	public static final int CODE_CONFIRMBACK = 1031;
	
	public static final int CODE_GETSHENFEN = 1050;
	public static final int CODE_GETBANKCARD = 1051;
	public static final int CODE_GETWORK = 1052;
	public static final int CODE_GETSCHOOL = 1053;
	public static final int CODE_GETCONTACT = 1054;
	
	
	//20171106
	//添加粒粒贷QQ客服
	public static final String RRSDQQ = "2409750841";
	//添加微信号
	public static final String RRSDWX = "lilidai1";
}
