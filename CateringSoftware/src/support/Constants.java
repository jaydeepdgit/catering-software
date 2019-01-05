/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

/**
 *
 * @author JD
 */
public class Constants {

    /*
     * To idenfity small size of frames
     */
    public static final java.util.List<Class> SMALL_SIZE_FRAMES = new java.util.ArrayList<>();

    static {
        SMALL_SIZE_FRAMES.add(utility.ChangePassword.class);
        SMALL_SIZE_FRAMES.add(utility.ManageUserView.class);
        SMALL_SIZE_FRAMES.add(utility.CompanySetting.class);
        SMALL_SIZE_FRAMES.add(master.TaxMaster.class);
        SMALL_SIZE_FRAMES.add(master.AccountType.class);
        SMALL_SIZE_FRAMES.add(master.AccountMaster.class);
        SMALL_SIZE_FRAMES.add(master.UnitMaster.class);
        SMALL_SIZE_FRAMES.add(utility.ManageEmail.class);
        SMALL_SIZE_FRAMES.add(utility.QuickOpen.class);
        SMALL_SIZE_FRAMES.add(master.BankMaster.class);
        SMALL_SIZE_FRAMES.add(utility.CheckPrint.class);
        SMALL_SIZE_FRAMES.add(master.RawMainCategory.class);
        SMALL_SIZE_FRAMES.add(master.RawSubCategory.class);
        SMALL_SIZE_FRAMES.add(master.RawMaterialMaster.class);
        SMALL_SIZE_FRAMES.add(master.FinishItemCommon.class);
        SMALL_SIZE_FRAMES.add(master.FunctionMaster.class);
    }

    /* form id - start */
    // MASTER
    public static final String ACCOUNT_TYPE_FORM_ID = "1";
    public static final String ACCOUNT_MASTER_FORM_ID = "2";
    public static final String MAIN_CATEGORY_RW_FORM_ID = "3";
    public static final String SUB_CATEGORY_RW_FORM_ID = "4";
    public static final String RAW_MATERIAL_RW_FORM_ID = "5";
    public static final String MAIN_CATEGORY_FI_FORM_ID = "6";
    public static final String FOOD_TYPE_FI_FORM_ID = "7";
    public static final String FINISH_MATERIAL_FI_FORM_ID = "8";
    public static final String TIME_MASTER_FORM_ID = "9";
    public static final String UNIT_MASTER_FORM_ID = "10";
    public static final String MENU_TYPE_MASTER_FORM_ID = "11";
    public static final String DRESS_CODE_MASTER_FORM_ID = "12";
    public static final String BANK_MASTER_FORM_ID = "13";
    public static final String TAX_MASTER_FORM_ID = "14";
    public static final String FUNCTION_MASTER_FORM_ID = "15";

    // EVENT
    public static final String EVENT_CATEGORY_FORM_ID = "61";
    public static final String EVENT_PACKAGE_FORM_ID = "62";

    // MENU ORDER
    public static final String MAIN_ORDER_FORM_ID = "101";
    public static final String ADD_ORDER_FORM_ID = "102";
    public static final String ADD_MULTIPLE_ORDER_FORM_ID = "103";
    public static final String ORDER_LIST_FORM_ID = "104";

    // REPORTS
    public static final String CHECK_PRINT_REPORT_FORM_ID = "211";
    public static final String ACCOUNT_LIST_FORM_ID = "212";

    // UTILITY
    public static final String COMPANY_SETTING_FORM_ID = "311";
    public static final String MANAGE_USER_FORM_ID = "312";
    public static final String USER_RIGHTS_FORM_ID = "313";
    public static final String MANAGE_EMAIL_FORM_ID = "314";
    public static final String CHANGE_PASSWORD_FORM_ID = "315";
    public static final String QUICK_OPEN_FORM_ID = "316";
    public static final String BACK_UP_FORM_ID = "317";
    public static final String RESET_FORM_ID = "318";
    public static final String EMAIL_FORM_ID = "319";
    public static final String CHECK_PRINT_FORM_ID = "320";
    public static final String NEW_YEAR_FORM_ID = "321";
    public static final String CHANGE_THEMES_FORM_ID = "322";

    /* form name: start */
    // LOGIN
    public static final String LOGIN_FORM_NAME = "LOGIN";
    public static final String LOG_OUT_FORM_NAME = "LOG OUT";
    public static final String EXIT_FORM_NAME = "EXIT";
    public static final String MINIMIZE_FORM_NAME = "MINIMIZE";

    // MASTER
    public static final String ACCOUNT_TYPE_FORM_NAME = "ACCOUNT TYPE";
    public static final String ACCOUNT_MASTER_FORM_NAME = "ACCOUNT MASTER";
    public static final String MAIN_CATEGORY_FORM_NAME = "MAIN CATEGORY";
    public static final String SUB_CATEGORY_FORM_NAME = "SUB CATEGORY";
    public static final String RAW_MATERIAL_FORM_NAME = "RAW MATERIAL";
    public static final String FOOD_TYPE_FORM_NAME = "FOOD TYPE";
    public static final String FINISH_MATERIAL_FORM_NAME = "FINISH MATERIAL";
    public final static String FINISH_ITEM_MAIN_CATEGORY_FORM_NAME = "FINISH ITEM " + MAIN_CATEGORY_FORM_NAME;
    public static final String TIME_MASTER_FORM_NAME = "TIME MASTER";
    public static final String UNIT_MASTER_FORM_NAME = "UNIT MASTER";
    public static final String MENU_TYPE_MASTER_FORM_NAME = "MENU TYPE MASTER";
    public static final String DRESS_CODE_MASTER_FORM_NAME = "DRESS CODE MASTER";
    public static final String BANK_MASTER_FORM_NAME = "BANK MASTER";
    public static final String TAX_MASTER_FORM_NAME = "TAX MASTER";
    public static final String FUNCTION_MASTER_FORM_NAME = "FUNCTION MASTER";

    // EVENT
    public static final String EVENT_CATEGORY_FORM_NAME = "EVENT CATEGORY";
    public static final String EVENT_PACKAGE_FORM_NAME = "EVENT PACKAGE";

    // MENU ORDER
    public static final String MAIN_ORDER_FORM_NAME = "MAIN ORDER";
    public static final String ADD_ORDER_FORM_NAME = "ADD ORDER";
    public static final String ADD_MULTIPLE_ORDER_FORM_NAME = "ADD MULTIPLE ORDER";
    public static final String ORDER_LIST_FORM_NAME = "ORDER LIST";

    // REPORTS
    public static final String CHECK_PRINT_REPORT_FORM_NAME = "CHECK PRINT REPORT";
    public static final String ACCOUNT_LIST_FORM_NAME = "ACCOUNT LIST";

    // UTILITY
    public static final String COMPANY_SETTING_FORM_NAME = "COMPANY SETTING";
    public static final String MANAGE_USER_FORM_NAME = "MANAGE USER";
    public static final String USER_RIGHTS_FORM_NAME = "USER RIGHTS";
    public static final String MANAGE_EMAIL_FORM_NAME = "MANAGE EMAIL";
    public static final String CHANGE_PASSWORD_FORM_NAME = "CHANGE PASSWORD";
    public static final String CHANGE_DATE_FORM_NAME = "CHANGE DATE";
    public static final String QUICK_OPEN_FORM_NAME = "QUICK OPEN";
    public static final String BACK_UP_FORM_NAME = "BACK UP";
    public static final String RESET_FORM_NAME = "RESET";
    public static final String EMAIL_FORM_NAME = "EMAIL";
    public static final String CHECK_PRINT_FORM_NAME = "CHECK PRINT";
    public static final String NEW_YEAR_FORM_NAME = "NEW YEAR";
    public static final String CHANGE_THEMES_FORM_NAME = "CHANGE THEMES";

    /* INITIAL Name: Start */
    // MASTER
    public static final String ACCOUNT_TYPE_INITIAL = "ACT";
    public static final String ACCOUNT_MASTER_INITIAL = "ACM";
    public static final String MAIN_CATEGORY_RM_INITIAL = "MCR";
    public static final String SUB_CATEGORY_RM_INITIAL = "SCR";
    public static final String RAW_MATERIAL_RM_INITIAL = "RMR";
    public static final String MAIN_CATEGORY_FI_INITIAL = "MCF";
    public static final String FINSH_FOOD_TYPE_INITIAL = "FFT";
    public static final String FINISH_MATERIAL_INITIAL = "FFM";
    public static final String TIME_MASTER_INITIAL = "TMM";
    public static final String UNIT_MASTER_INITIAL = "UNM";
    public static final String MENU_TYPE_MASTER_INITIAL = "MTM";
    public static final String DRESS_CODE_MASTER_INITIAL = "DCM";
    public static final String BANK_MASTER_INITIAL = "BNM";
    public static final String TAX_MASTER_INITIAL = "TAX";

    // EVENT
    public static final String FUNCTION_MASTER_INITIAL = "FTM";
    public static final String EVENT_PACKAGE_INITIAL = "ENP";

    // MENU ORDER
    public static final String MAIN_ORDER_INITIAL = "MNO";
    public static final String ADD_ORDER_INITIAL = "ADO";
    public static final String ADD_MULTIPLE_ORDER_INITIAL = "ADM";

    // UTILITY
    public static final String COMPANY_SETTING_INITIAL = "CM";
    public static final String CHECK_PRINT_INITIAL = "CHP";

    // OTHER
    public static final String OPB_INITIAL = "OPB";

    /* OTHER CONSTANTS: Start */
    public static final String NO_TAX = "TAX00001";
    public static final String SELECT_DATE = "SELECT DATE";
    public static final String HOME_PAGE = "HOME PAGE";
    public static final String COVER_PRINT = "COVER PRINT";
    public static final String SOFTWARE_NAME = "CATERING MANAGEMENT";
    public static final String VERSION = "1.0.0";
    public static final String MASTER = "MASTER";
    public static final String REPORTS = "REPORTS";
    public static final String UTILITY = "UTILITY";
    public static final String COMPANY_SELECT = "COMPANY SELECT";
    public static final String MENU_MASTER_FORM_NAME = "MENU MASTER";
    public static final String FORM_MASTER_FORM_NAME = "FORM MASTER";
    public static final String NEW_COMPANY_FORM_NAME = "NEW COMPANY";
    public static final String EMAIL_BUTTON = "E-MAIL";
    public static final String HELP = "HELP";

    // Messages
    public static final String NO_RIGHTS_TO_VIEW = "You have no rights to view";
    public static final String SELECT_ROW = "Please Select Any Row.";
    public static final String DELETE_ROW = "Do you want to delete this row ";
    public static final String DELETE_RECORD = "Do you want to delete this record?";
    public static final String DELETE_THIS = "Do you want to delete this ";
    public static final String INVALID_ACCOUNT = "Invalid Account Name";
    public static final String INVALID_BANK = "Invalid Bank Name";
    public static final String ACCOUNT_NOT_BLANK = "Account should not be blank";
    public static final String ACCOUNT_NOT_EXIST = "Account does not exist";
    public static final String VOUCHER_NOT_BLANK = "Voucher should not be blank";
    public static final String INVALID_AMOUNT = "Invalid Amount";
    public static final String INVALID_CHEQUE_DATE = "Invalid Cheque Date";
    public static final String INVALID_VOUCHER_DATE = "Invalid Voucher Date";
    public static final String INVALID_DELIVERY_DATE = "Invalid Delivery Date";
    public static final String INVALID_VOUCHER_NO = "voucher no is invalid";
    public static final String SAVE_SUCCESS = "Save Successfully";
    public static final String ADD_MORE_ENTRY = "Do you want to add more entry?";
    public static final String CORRECT_DATE = "Enter Correct Date";
    public static final String INVALID_ITEM = "Invalid Item Name";
    public static final String INVALID_QTY = "Invalid Qty";
    public static final String INVALID_RATE = "Invalid Rate";
    public static final String INVALID_DATE = "Invalid Date";
    public static final String INVALID_DISCOUNT = "Invalid Disc";
    public static final String INVALID_C_GST = "Invalid C GST";
    public static final String INVALID_S_GST = "Invalid S GST";
    public static final String INVALID_I_GST = "Invalid I GST";

    public static final String drName = "Dr";
    public static final String crName = "Cr";

	public enum Tables {
		FINISH_ITEM("finish_item_main", FINISH_ITEM_MAIN_CATEGORY_FORM_NAME, MAIN_CATEGORY_FI_INITIAL, MAIN_CATEGORY_FI_FORM_ID),
		FOOD_TYPE("food_type", FOOD_TYPE_FORM_NAME, FINSH_FOOD_TYPE_INITIAL, FOOD_TYPE_FI_FORM_ID);
		final public String NAME, FRAME_TITLE, PREFIX, FORM_ID;
			Tables(String tableName, String frameTitle, String prefix, String formId) {
			this.NAME = tableName;
			this.FRAME_TITLE = frameTitle;
			this.PREFIX = prefix;
			this.FORM_ID = formId;
		}
	}
}