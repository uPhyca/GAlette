/*
 * Copyright (C) 2014 uPhyca Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uphyca.galette;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.*;

public class GAletteInstrumentation {

    static class ModifierClassWriter extends ClassVisitor {

        private String name;
        private final boolean[] proceed = new boolean[1];

        ModifierClassWriter(int api, ClassVisitor cv) {
            super(api, cv);
        }

        /**
         * Returns true if the class transformed.
         */
        boolean isProceed() {
            return proceed[0];
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            this.name = name;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
            ModifierMethodWriter mvw = new ModifierMethodWriter(api, this.name, mv, access, name, desc, proceed);
            return mvw;
        }

        @Override
        public void visitEnd() {
            if (proceed[0]) {
                visitAnnotation("Lcom/uphyca/galette/GAlette$Baked;", false);
            }
            super.visitEnd();
        }
    }


    public static class ModifierMethodWriter extends AdviceAdapter {

        private final String className;
        private final String methodName;
        private final Type[] argumentTypes;

        ModifierMethodWriter(int api, String className, MethodVisitor mv, int access, String methodName, String desc, boolean[] proceed) {
            super(api, mv, access, methodName, desc);
            this.className = className;
            this.methodName = methodName;
            this.argumentTypes = Type.getArgumentTypes(desc);
            this.proceed = proceed;
        }

        private final boolean[] proceed;
        private String trackingMethodName;

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            AnnotationVisitor av = super.visitAnnotation(desc, visible);
            if (desc.equals("Lcom/uphyca/galette/SendAppView;")) {
                trackingMethodName = "sendAppView";
            } else if (desc.equals("Lcom/uphyca/galette/SendEvent;")) {
                trackingMethodName = "sendEvent";
            }
            return av;
        }

        @Override
        protected void onMethodEnter() {
            if (trackingMethodName == null) {
                super.onMethodEnter();
                return;
            }

            proceed[0] = true;

            // The local variable to store argument types
            final int argumentTypesVariableId = newLocal(Type.getObjectType("[Ljava/lang/Class;"));
            // The local variable to store method
            final int methodVariableId = newLocal(Type.getObjectType("java/lang/reflect/Method"));
            // The local variable to store argument values
            final int argumentValuesVariableId = newLocal(Type.getObjectType("[Ljava/lang/Object;"));

            // Allocate the array to store argument values.
            // e.g. Object[] argumentValues = new Object[arguments.length]
            pushInt(argumentTypes.length);
            newArray(Type.getObjectType("java/lang/Object"));
            storeLocal(argumentValuesVariableId);

            // Store each argument values to the array
            // e.g. argumentValues[index] = arguments[index]
            for (int i = 0, size = argumentTypes.length; i < size; ++i) {
                loadLocal(argumentValuesVariableId);
                pushInt(i);
                loadArg(i);
                box(argumentTypes[i]);
                arrayStore(Type.getObjectType("java/lang/Object"));
            }

            // Allocate the array to store argument types
            // e.g. Class[] argumentTypes = new Class[arguments.length]
            pushInt(argumentTypes.length);
            newArray(Type.getObjectType("java/lang/Class"));
            storeLocal(argumentTypesVariableId);

            // Store each argument types to the array
            // e.g. argumentTypes[index] = arguments[index].getClass()
            for (int i = 0, size = argumentTypes.length; i < size; ++i) {
                loadLocal(argumentTypesVariableId);
                pushInt(i);
                pushClassObject(argumentTypes[i]);
                arrayStore(Type.getObjectType("java/lang/Class"));
            }

            // Get the method
            // e.g. Method method = Foo.class.getDeclaredMethod(methodName, argumentTypes)
            visitLdcInsn(Type.getObjectType(className));
            visitLdcInsn(methodName);
            loadLocal(argumentTypesVariableId);
            visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
            storeLocal(methodVariableId);

            // Invoke the tracking method
            // e.g. GAlette.sendAppView(owner, method, argumentValues)
            loadThis();
            loadLocal(methodVariableId);
            loadLocal(argumentValuesVariableId);
            visitMethodInsn(Opcodes.INVOKESTATIC, "com/uphyca/galette/GAlette", trackingMethodName, "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)V", false);

            visitMaxs(0, 0);
            super.onMethodEnter();
        }


        /**
         * Push type to statck
         */
        private void pushClassObject(Type type) {
            switch (type.getSort()) {
                case Type.BOOLEAN:
                    visitTypeField("java/lang/Boolean");
                    break;
                case Type.BYTE:
                    visitTypeField("java/lang/Byte");
                    break;
                case Type.CHAR:
                    visitTypeField("java/lang/Character");
                    break;
                case Type.SHORT:
                    visitTypeField("java/lang/Short");
                    break;
                case Type.INT:
                    visitTypeField("java/lang/Integer");
                    break;
                case Type.FLOAT:
                    visitTypeField("java/lang/Float");
                    break;
                case Type.LONG:
                    visitTypeField("java/lang/Long");
                    break;
                case Type.DOUBLE:
                    visitTypeField("java/lang/Double");
                    break;
                default:
                    visitLdcInsn(type);
                    break;
            }
        }

        /**
         * Visit TYPE field
         */
        private void visitTypeField(String internalClassName) {
            visitFieldInsn(GETSTATIC, internalClassName, "TYPE", "Ljava/lang/Class;");
        }

        /**
         * Push integer to statck
         */
        private void pushInt(int i) {
            if (i <= 5) {
                visitInsn(Opcodes.ICONST_0 + i);
            } else {
                visitIntInsn(Opcodes.BIPUSH, i);
            }
        }
    }

    static class BakedAnnotationReader extends ClassVisitor {

        private boolean weaved;

        BakedAnnotationReader(int api) {
            super(api);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            if (desc.equals("Lcom/uphyca/galette/GAlette$Baked;")) {
                weaved = true;
                visitEnd();
            }
            return super.visitAnnotation(desc, visible);
        }
    }

    /**
     * Process the file
     */
    public void processFile(File classFile) throws Exception {
        if (classFile.isDirectory()) {
            throw new IllegalArgumentException(classFile + " is not a directory");
        }
        InputStream in = null;
        try {
            in = new FileInputStream(classFile);
            final ClassReader classReader = new ClassReader(in);
            if (!hadWeaved(classReader)) {
                weaveIfNecessary(classReader, classFile);
            }
        } finally {
            closeQuietly(in);
        }
    }

    /**
     * Weave tracking method call if the annotations present.
     */
    private static void weaveIfNecessary(ClassReader classReader, File dest) throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ModifierClassWriter mcw = new ModifierClassWriter(Opcodes.ASM5, cw);
        classReader.accept(new CheckClassAdapter(mcw), ClassReader.EXPAND_FRAMES);
        if (!mcw.isProceed()) {
            return;
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(dest);
            out.write(cw.toByteArray());
            out.flush();
        } finally {
            closeQuietly(out);
        }
    }

    /**
     * Returns true if the class had weaved.
     */
    private static boolean hadWeaved(ClassReader classReader) {
        BakedAnnotationReader annotationReadClassVisitor = new BakedAnnotationReader(Opcodes.ASM5);
        classReader.accept(annotationReadClassVisitor, ClassReader.SKIP_CODE);
        return annotationReadClassVisitor.weaved;
    }

    /**
     * Close the resource without IOException
     */
    private static void closeQuietly(Closeable res) {
        if (res == null) {
            return;
        }
        try {
            res.close();
        } catch (IOException ignore) {
        }
    }

    public void processFiles(File fileOrDirectory) throws Exception {
        if (fileOrDirectory.isFile()) {
            if (fileOrDirectory.getName().endsWith(".class")) {
                processFile(fileOrDirectory);
            }
        } else {
            for (File each : fileOrDirectory.listFiles()) {
                processFiles(each);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final GAletteInstrumentation inst = new GAletteInstrumentation();
        File f = new File(args[0]);
        if (!f.exists()) {
            throw new IllegalArgumentException(f + " does not exists");
        }
        inst.processFiles(f);
    }
}
