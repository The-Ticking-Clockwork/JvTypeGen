package io.github.yu_vitaqua_fer_chronos.jvtypegen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] descs) {
        // Iterates through all arguments given, accepts class descriptors
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        for (String descriptor : descs) {
            // Prints the serialised `ClassInfo` class returned from `analyseClass`
            Main.saveJsonToFile(descriptor, gson.toJson(analyseClass(descriptor)));
        }
    }

    private static void saveJsonToFile(String descriptor, String content) {
        String fileName = descriptor.replace('.', '/');

        // Removes any `[` indicating an array
        while (fileName.startsWith("[")) {
            fileName = fileName.substring(1);
        }

        // `L` means a JVM type, needs to be stripped for analysis
        if (fileName.startsWith("L")) {
            fileName = fileName.substring(1);
        }

        // `;` means the end of a type descriptor, needs to be stripped for analysis
        if (fileName.endsWith(";")) {
            fileName = fileName.substring(0, fileName.length()-1);
        }

        // Innerclasses are also files
        fileName = fileName.replace('$', '/');
        // Since we're saving JSON, use the correct format
        fileName += ".json";
        // Cleaner save location
        fileName = "json/" + fileName;

        Path savePath = Paths.get("output", fileName.split("/"));

        try {
            Files.createDirectories(savePath.getParent());
            Files.writeString(savePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write to `" + savePath + "` for some reason!", e);
        }
    }

    private static ClassInfo analyseClass(String descriptor) {
        // Analyses a class to get info such as the methods, fields, constructors, etc within
        String clsDesc = descriptor;
        int arrayDepth = 0;

        // Removes any `[` indicating an array
        while (clsDesc.startsWith("[")) {
            clsDesc = clsDesc.substring(1);
            arrayDepth += 1;
        }

        // `L` means a JVM type, needs to be stripped for analysis
        if (clsDesc.startsWith("L")) {
            clsDesc = clsDesc.substring(1);
        }

        // `;` means the end of a type descriptor, needs to be stripped for analysis
        if (clsDesc.endsWith(";")) {
            clsDesc = clsDesc.substring(0, clsDesc.length()-1);
        }

        // We get the current classloader so we can load the JVM type and analyse it
        ClassLoader clsLoader = Main.class.getClassLoader();

        // Uninitialised variable
        Class<?> cls;

        // try-catch block to attempt to load the class, and to crash if it can't
        try {
            cls = clsLoader.loadClass(clsDesc.replace('/', '.'));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class `" + clsDesc.replace('/', '.') +
                    "` could not be found!", e);
        }

        // Get the class name (not package)
        String clsName = cls.getName();

        // Interfaces the class implements
        List<String> implClses = new ArrayList<>();

        // Get all interfaces the class uses
        for (Class<?> implCls : cls.getInterfaces()) {
            implClses.add(implCls.getName());
        }

        // Classes such as `Object` (likely the only class?) don't have a superclass
        String superCls = "";

        // Get the superclass if it exists
        if(cls.getSuperclass() != null) {
            superCls = cls.getSuperclass().getName();
        }

        ClassInfo.ClassType clsType = ClassInfo.ClassType.getValue(cls);

        // Initialise the lists for constructors, methods and fields
        List<ClassInfo.ConstructorInfo> constructors = new ArrayList<>();
        ArrayList<ClassInfo.MethodInfo> methods = new ArrayList<>();
        ArrayList<ClassInfo.FieldInfo> fields = new ArrayList<>();

        // Iterate through all constructors in the class
        for (Constructor<?> c : cls.getConstructors()) {
            LinkedHashMap<String, String> params = new LinkedHashMap<>();

            // Used to see if the constructor exists in the parent class (if there is a parent)
            ArrayList<Class<?>> paramTypes = new ArrayList<>();

            for (Parameter p : c.getParameters()) {
                params.put(p.getName(), p.getType().getName());

                if (!superCls.equals("")) {
                    paramTypes.add(p.getType());
                }
            }

            // Checks if the superclass exists, then checks if the constructor exists for the class
            if (!superCls.equals("")) {
                try {
                    cls.getSuperclass().getDeclaredConstructor(paramTypes.toArray(new Class[]{}));
                } catch (NoSuchMethodException e) {
                    constructors.add(new ClassInfo.ConstructorInfo(params));
                }
            } else {
                constructors.add(new ClassInfo.ConstructorInfo(params));
            }
        }

        for (Method m : cls.getMethods()) {
            String methodName = m.getName();
            String returnType = m.getReturnType().getSimpleName();

            boolean isStatic = Modifier.isStatic(m.getModifiers());

            LinkedHashMap<String, String> params = new LinkedHashMap<>();

            // Used to see if the constructor exists in the parent class (if there is a parent)
            ArrayList<Class<?>> paramTypes = new ArrayList<>();

            for (Parameter p : m.getParameters()) {
                params.put(p.getName(), p.getType().getName());

                if (!superCls.equals("")) {
                    paramTypes.add(p.getType());
                }
            }

            // Checks if the superclass exists, then checks if the method exists for the class
            if (!superCls.equals("")) {
                try {
                    cls.getSuperclass().getDeclaredMethod(methodName, paramTypes.toArray(new Class[]{}));
                } catch (NoSuchMethodException e) {
                    methods.add(new ClassInfo.MethodInfo(methodName, params, returnType, isStatic));
                }
            } else {
                methods.add(new ClassInfo.MethodInfo(methodName, params, returnType, isStatic));
            }
        }

        for (Field f : cls.getFields()) {
            // Check if the superclass exists, and if it does, check if the field exists in the superclass
            if (!superCls.equals("")) {
                try {
                    cls.getSuperclass().getDeclaredField(f.getName());
                } catch (NoSuchFieldException e) {
                    fields.add(new ClassInfo.FieldInfo(f.getName(), f.getType().getName(),
                            Modifier.isStatic(f.getModifiers())));
                }
            } else {
                fields.add(new ClassInfo.FieldInfo(f.getName(), f.getType().getName(),
                        Modifier.isStatic(f.getModifiers())));
            }
        }

        return new ClassInfo(
                cls.descriptorString(),
                clsName,
                clsType,
                superCls,
                implClses,
                constructors,
                methods,
                fields
        );
    }
}
