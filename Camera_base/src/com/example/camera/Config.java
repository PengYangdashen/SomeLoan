package com.example.camera;

public class Config {
	public static final String URL = "http://www.wekeapp.com/";
	public static final String UPDATE_CODE = "apk/updatezld.json";
	public static final String SHARELOGO_CODE = "page/images/zldd.png ";

	public static final String REGISTERADDRES_CODE = URL
			+ "wx/main/zldzcxy.html";
	public static final String OUTMONEYPROTOL_CODE = URL
			+ "wx/main/zldjkxy.html";
	public static final String BANKCARDPROTOL_CODE = URL
			+ "wx/main/zldrzxy.html";
	public static final String PROLEM_CODE = URL
			+ "androidHtml/zldcjwt_app.html";

	public static final String LOGIN_CODE = "servlet/current/JBDUserAction?function=LoginZLD";
	public static final String REGISTER = "servlet/current/JBDUserAction?function=RegisterNewZLD";
	public static final String SEND_CODE = "servlet/current/JBDUserAction?function=SendMsgNewZLD";
	public static final String FORGET_CODE = "servlet/current/JBDUserAction?function=FindPasswordZLD";
	public static final String FORGET_SEND_CODE = "servlet/current/JBDUserAction?function=SendMsgNewFindPwdZLD";
	public static final String CHECK_CODE = "servlet/current/JBDUserAction?function=ZLDFindPasswordZLD";
	public static final String HOME_INIT = URL
			+ "servlet/current/JBDUserAction?function=FindUsernameZLD";
	public static final String OUTMONEY_RECORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDGetShowJD";
	public static final String PAY_RESULT_CORD = URL
			+ "servlet/current/JBDModelAction?function=ZLDgetAuthResultApp";
	public static final String PAY_START_CORD = URL
			+ "servlet/current/JBDModelAction?function=ZLDAppPayAuth";
	public static final String RELATION_INFO_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDAddContactIofo";
	public static final String WORK_INFO_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDAddJobIofo";
	public static final String CARD_BANAME_CORD = URL
			+ "servlet/current/JBDUserAction";
	public static final String IWANTMONEY_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDAddUserJK";
	public static final String AUTHMOBILE_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDGetConversation";
	public static final String AUTHTAOBAO_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDGetTabao";
	public static final String AUTHJD_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDGetJD";

	public static final String JKSEND_CORD = URL
			+ "servlet/current/JBDUserAction?function=SendGSDJkMsg";

	public static final String JKRECORD_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDShowJKJD&type=0";
	public static final String MONEYRECORDE_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDShowTGInfo";
	public static final String VIDEO_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDGetSPSH";
	public static final String SHARE_CORD = URL + "page/zld.html?";
	public static final String BACKMONEYINIT_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDRepaymentJK";
	public static final String BACKMONEY_CORD = URL
			+ "servlet/current/JBDModelAction?function=ZLDRepaymentJKMoney";
	public static final String ASKMONEYINIT_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDGetYHKT";
	public static final String ASKMONEY_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDTjJKMoney";
	public static final String NEWS_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDFindMsg";
	public static final String ZMSTART_CORD = URL
			+ "servlet/current/JBDcms2Action?function=GetZmyRzZLD";
	public static final String ZMEND_CORD = URL
			+ "servlet/current/JBDcms2Action?function=ZmyRzZLD";
	public static final String COMMITINVESTER_CORD = URL
			+ "servlet/current/JBDInvestAction?function=AddInvestorZLD";
	public static final String BORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDGetInvestList";
	public static final String BORROWPERSONDETAIL_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDToInvest";
	public static final String CHECKBORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDSubmitInvestment";
	public static final String CANCLEBORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDInvestmentReturn";
	public static final String PAYBORROWPERSON_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDInvestmentResults";
	public static final String INVESTMONEY_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDInvestmentInformation";
	public static final String INVESTMONEYRECORDE_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDonloan";
	public static final String OVERDUELIST_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDOverdueList";
	public static final String OVERDUEPHONE_CORD = URL
			+ "servlet/current/JBDInvestAction?function=ZLDTocollect";
	public static final String TXMONEY_CORD = URL
			+ "servlet/current/JBDUserAction?function=ZLDPresentationRecord";
	public static final String PAY_RESULTLL_CORD = URL
			+ "servlet/current/JBDLLpayAction?function=ZLDGetAuthResultApp";// 连连支付
	public static final String PAY_LLSTART_CORD = URL
			+ "servlet/current/JBDLLpayAction?function=ZLDAppPayAuth";// 连连支付
	public static final String BACKMONEYLL_CORD = URL
			+ "servlet/current/JBDLLpayAction?function=ZLDRepaymentJKMoney";// 连连支付
	public static final String CONFIRMBM_CORD = URL
			+ "servlet/current/JBDcms3Action?function=ZLDRepayment";
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
	public static final int CODE_PAY_RESULT = 1009;
	public static final int CODE_PAY_START = 1010;
	public static final int CODE_RELATION_INFO = 1011;
	public static final int CODE_WORK_INFO = 1012;
	public static final int CODE_CARD_MANAGE = 1013;
	public static final int CODE_IWANTMONEY = 1014;
	public static final int CODE_AUTHMOBILE = 1015;
	public static final int CODE_JKSEND = 1016;
	public static final int CODE_JKRECORD = 1017;
	public static final int CODE_MONEYRECORDE = 1018;
	public static final int CODE_VIDEO = 1019;
	public static final int CODE_SHARE = 1020;
	public static final int CODE_BACKMONEY = 1021;
	public static final int CODE_BACKMONEYINIT = 1022;
	public static final int CODE_ASKMONEYINIT = 1023;
	public static final int CODE_ASKMONEY = 1024;
	public static final int CODE_NEWS = 1025;
	public static final int CODE_BANK = 1026;
	public static final int CODE_ZMSTART = 1027;
	public static final int CODE_ZMEND = 1028;
	public static final int CODE_TXMONEY = 1029;
	public static final int CODE_CONFIRMBACK = 1031;

}
