package io.github.yu_vitaqua_fer_chronos.jvtypegen;

import java.util.HashMap;
import java.util.List;

public class ClassInfo {
    public String name;
    public ClassType type;
    public String superClass;
    public List<String> implementationClasses;
    public String descriptorString;
    public List<ConstructorInfo> constructors;
    public List<MethodInfo> methods;
    public List<FieldInfo> fields;

    public ClassInfo(String descriptorString, String name, ClassType type, String superClass,
                     List<String> implementationClasses, List<ConstructorInfo> constructors, List<MethodInfo> methods,
                     List<FieldInfo> fields) {
        this.descriptorString = descriptorString;
        this.name = name;
        this.type = type;
        this.superClass = superClass;
        this.implementationClasses = implementationClasses;
        this.constructors = constructors;
        this.methods = methods;
        this.fields = fields;
    }

    public enum ClassType {
        CLASS, ENUM, INTERFACE;

        public static ClassType getValue(Class<?> cls) {
            if (cls.isEnum()) {
                return ENUM;
            } else if (cls.isInterface()) {
                return INTERFACE;
            } else {
                return CLASS;
            }
        }
    }

    public static class ConstructorInfo {
        public HashMap<String, String> params;

        public ConstructorInfo(HashMap<String, String> params) {
            this.params = params;
        }
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
