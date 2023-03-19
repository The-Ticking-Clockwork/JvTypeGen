package io.github.yu_vitaqua_fer_chronos.jvtypegen;

import java.util.HashMap;
import java.util.List;

public class ClassInfo {
    public String name;
    public String descriptorString;
    public List<MethodInfo> methods;
    public List<FieldInfo> fields;

    public ClassInfo(String name, String descriptorString, List<MethodInfo> methods,
                     List<FieldInfo> fields) {
        this.name = name;
        this.descriptorString = descriptorString;
        this.methods = methods;
        this.fields = fields;
    }

    public static class MethodInfo {
        public String name;
        public HashMap<String, String> params;
        public String returnType;
        public boolean isStatic;

        public MethodInfo(String name, HashMap<String, String> params, String returnType,
                          boolean isStatic) {
            this.name = name;
            this.params = params;
            this.returnType = returnType;
            this.isStatic = isStatic;
        }
    }

    public static class FieldInfo {
        public String name;
        public String type;
        public boolean isStatic;

        public FieldInfo(String name, String type,
                          boolean isStatic) {
            this.name = name;
            this.type = type;
            this.isStatic = isStatic;
        }
    }
}
