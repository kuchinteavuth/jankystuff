package com.inteavuthkuch.jankystuff.common;

public enum JankyBlockEntityType {
    ADVANCED_QUARRY(1),
    INVALID(-1)
    ;

    private final int typeCode;
    JankyBlockEntityType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public static JankyBlockEntityType get(int typeCode) {
        for(JankyBlockEntityType type : JankyBlockEntityType.values()){
            if(type.getTypeCode() == typeCode)
                return type;
        }
        return INVALID;
    }
}
