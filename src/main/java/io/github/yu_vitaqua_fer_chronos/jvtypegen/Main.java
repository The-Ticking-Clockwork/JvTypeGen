package io.github.yu_vitaqua_fer_chronos.jvtypegen;

import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] descs) {
        Gson gson = new Gson();

        Set<Class<?>> needsAnalysis = new HashSet<>();
        for (String descriptor : descs) {
            System.out.println(gson.toJson(analyseClass(descriptor)));
        }
    }

    public static ClassInfo analyseClass(String descriptor) {
        String clsDesc = descriptor;
        int arrayDepth = 0;

        while (clsDesc.startsWith("[")) {
            clsDesc = clsDesc.substring(1);
            arrayDepth += 1;
        }

        if (clsDesc.startsWith("L")) {
            clsDesc = clsDesc.substring(1);
        }

        if (descriptor.endsWith(";")) {
            clsDesc = clsDesc.substring(0, clsDesc.length()-1);
        }

        ClassLoader clsLoader = Main.class.getClassLoader();

        Class cls;

        try {
            cls = clsLoader.loadClass(clsDesc.replace('/', '.'));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class `" + clsDesc.replace('/', '.') +
                    "` could not be found!", e);
        }

        String clsName = cls.getCanonicalName();

        ArrayList<ClassInfo.MethodInfo> methods = new ArrayList<>();
        ArrayList<ClassInfo.FieldInfo> fields = new ArrayList<>();

        for (Method m : cls.getMethods()) {
            String methodName = m.getName();
            String returnType = m.getReturnType().getSimpleName();

            boolean isStatic = Modifier.isStatic(m.getModifiers());

            HashMap<String, String> params = new HashMap<>();

            for (Parameter p : m.getParameters()) {
                params.put(p.getName(), p.getType().getSimpleName());
            }

            methods.add(new ClassInfo.MethodInfo(methodName, params, returnType, isStatic));
        }

        for (Field f : cls.getFields()) {
            fields.add(new ClassInfo.FieldInfo(f.getName(), f.getType().getSimpleName(),
                    Modifier.isStatic(f.getModifiers())));
        }

        return new ClassInfo(clsName, descriptor, methods, fields);
    }
}
