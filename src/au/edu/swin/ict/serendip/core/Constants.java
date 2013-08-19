package au.edu.swin.ict.serendip.core;

public interface Constants {
    // File names paths etc.
    public static final String SERENDIP_ROMEO_EXE_PROP = "SERENDIP_ROMEO_EXE";
    public static final String SERENDIP_TCTL_FILE_EXT = ".tctl";
    public static final String SERENDIP_PETRINET_FILE_EXT = ".xml";
    public static final String SERENDIP_COMPOSITION_FILE_EXT = ".xml";
    public static final String SERENDIP_SHOW_ADMIN_VIEW = "SERENDIP_SHOW_ADMIN_VIEW";
    public static final String SERENDIP_COMPOSITE_DIR = "road_composites";
    // Symbols
    public static final String SERENDIP_SYMBOL_AND = "*";// These need to sync
							 // with ep.g and
							 // behav.g antlr
							 // grammar
    public static final String SERENDIP_SYMBOL_OR = "|";
    public static final String SERENDIP_SYMBOL_XOR = "^";

    // descriptor
    public static final String SERENDIP_PATH_SEP = "_";

    // /SCRIPTING RELATED
    // Following are the constants that are being used in scripts
    public enum commands {
	updatePropertyOfTask, updatePropertyOfProcessInstance, addConstraintToInstance, addNewTaskToInstance, executeScript, updateProcessInstanceState, updateStateOfAllProcessInstances, pause, resume,
	// DEF LEVEL
	applyPatch, // the method to apply a patch. Ease of use.
	addRole, updateRole, removeRole, addContract, updateContract, removeContract, addOperationToTerm, setOutMsgOnTask, setInMsgOnTask, addTaskDef, updateTaskDef, removeTaskDef, addTaskRef, updateTaskRef, removeTaskRef, addConstraintToBehavior, updateConstraintToBehavior, removeConstraintToBehavior, addTerm, updateTerm, removeTerm, addPlayerBinding, removePlayerBinding, updatePlayerBinding, addBehavior, removeBehavior, updateBehavior, addProcessDef, removeProcessDef, updateProcessDef, removeBehaviorRefFromProcessDef, addBehaviorRefToProcessDef, expandTheRole, scaleInTheComposite

    }

    // Following are the scopes that are being used in scripts
    public enum scope {
	PI, DEF
    }

    // PatchFile directory
    public static final String PATCH_FILE_DIR = "patches";
    // Following are the constants that are being used in the script as keys in
    // key=val pairs
    public static final String KEY_PID = "pId";
    public static final String KEY_TID = "tId";
    public static final String KEY_PDID = "pdId";
    public static final String KEY_BTID = "bId";
    public static final String KEY_CID = "cId";
    public static final String KEY_PROPERTY = "property";// ...property=...
    public static final String KEY_VALUE = "value"; // ...value=...
    public static final String KEY_STATUS = "status";
    public static final String KEY_ENABLED = "enabled";

    public static final String VAL_TRUE = "true";
    public static final String VAL_FALSE = "false";

    public static final String KEY_PREEP_VAL = "preep";
    public static final String KEY_POSTEP_VAL = "postep";
    public static final String KEY_OBLIGROLE_VAL = "obligrole";
    public static final String KEY_PP_VAL = "pp";

    public static final String KEY_ROLE_ID = "rId";
    public static final String KEY_ROLE_NAME = "name";
    public static final String KEY_ROLE_DESCR = "roleDescr";

    public static final String KEY_PATCH_FILE = "patchFile";
    public static final String KEY_CONTRACT_ID = "cId";
    public static final String KEY_CONTRACT_NAME = "name";
    public static final String KEY_CONTRACT_DESCR = "description";
    public static final String KEY_CONTRACT_STATE = "state";
    public static final String KEY_CONTRACT_TYPE = "type";
    public static final String KEY_CONTRACT_RULEFILE = "ruleFile";
    public static final String KEY_CONTRACT_ISABSTRACT = "isAbstract";
    public static final String KEY_CONTRACT_ROLEAID = "rAId";
    public static final String KEY_CONTRACT_ROLEBID = "rBId";
    public static final String KEY_TERM_ID = "tmId";
    public static final String KEY_TERM_CTID = "cId";
    public static final String KEY_TERM_NAME = "name";
    public static final String KEY_TERM_OP_NAME = "name";
    public static final String KEY_TERM_DESCR = "description";
    public static final String KEY_TERM_MESSAGE_TYPE = "messageType";
    public static final String KEY_TERM_DEONTIC_TYPE = "deonticType";
    public static final String KEY_TERM_DIRECTION = "direction";
    public static final String KEY_TERM_RETURN_TYPE = "returnType";
    public static final String KEY_TERM_PARAMETERS = "parameters";

    public static final String KEY_TASK_RETURN_TYPE = "returnType";
    public static final String KEY_TASK_PARAMETERS = "parameters";
    public static final String KEY_TASK_OP_NAME = "name";
    public static final String KEY_TASK_DELIVERY_TYPE = "deliveryType";
    public static final String KEY_TASK_IS_RESPONSE = "deliveryType";
    // Following are the property names
    public static final String KEY_PD_COS = "CoS";
    public static final String KEY_PD_COT = "CoT";

    public static final String KEY_BU_EXTENDSFROM = "extendsFrom";
    public static final String KEY_BU_ISABSTRACT = "isAbstract";

    public static final String KEY_CT_ISENABLED = "IsEnabled";
    public static final String KEY_CT_EXPRESSION = "Expression";
    public static final String KEY_CT_LANG = "Language";

    public static final String KEY_PB_ID = "pbId";
    public static final String KEY_PB_RID = "rId";
    public static final String KEY_PB_ENDPOINT = "endpoint";

    public static final String KEY_BU_ID = "bId";
    public static final String KEY_BU_PREEP = "preEP";
    public static final String KEY_BU_POSTEP = "postEP";
    public static final String KEY_BU_PP = "pp";
    public static final String KEY_BU_CID = "conId";
    public static final String KEY_BU_CEXPR = "cExpr";
    public static final String KEY_BU_ENABLED = "enabled";

    public static final String KEY_R_ID = "rId";
    public static final String KEY_TRANS_FILE = "transFile";
    public static final String KEY_TASK_ID = "tId";
    public static final String KEY_SRC_MGS = "usingMsgs";
    public static final String KEY_RESULT_MGS = "resultingMsgs";

    public static final String KEY_CUSTOM_CLASS = "class";

    // Following are the composite properties
    public static final String COMP_PROP_HOST = "Host";
}
