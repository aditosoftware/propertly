package de.adito.propertly.core.api;

import de.adito.propertly.core.spi.IPropertyDescription;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.*;

import java.util.*;

import static org.objectweb.asm.Opcodes.*;

public final class NodeChildrenGenerator
{
  private static int sClassCounter = 0;

  private NodeChildrenGenerator() {}

  @NotNull
  public static Class<? extends NodeChildren> generate(@NotNull Set<IPropertyDescription> pPropertyDescriptions)
  {
    List<IPropertyDescription> descriptions = new ArrayList<>(pPropertyDescriptions);
    String className = "de.adito.propertly.core.api.NodeChildren$Static_" + (sClassCounter++);
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    String internalName = className.replace('.', '/');

    cw.visit(V1_8, ACC_PUBLIC, internalName, null,
             "de/adito/propertly/core/api/AbstractNodeChildren", null);

    cw.visitField(ACC_PRIVATE, "fNodes", "[Lde/adito/propertly/core/api/INode;", null, null).visitEnd();
    cw.visitField(ACC_PRIVATE | ACC_STATIC, "fFieldNames", "[Ljava/lang/String;", null, null).visitEnd();

    List<String> fieldNames = new ArrayList<>();
    for (IPropertyDescription desc : descriptions) {
      String fieldName = "f_" + desc.getName().replaceAll("[^a-zA-Z0-9_]", "_");
      fieldNames.add(fieldName);
      cw.visitField(ACC_PRIVATE, fieldName, "Lde/adito/propertly/core/api/INode;", null, null).visitEnd();
    }

    int size = descriptions.size();

    generateClinit(cw, internalName, size, descriptions);
    generateConstructor(cw, internalName, size, fieldNames, descriptions);
    generateClear(cw, internalName, fieldNames);
    generateMutationMethods(cw, internalName);
    generateFindString(cw, internalName, descriptions, fieldNames);
    generateFindDescription(cw, internalName, descriptions, fieldNames);
    generateGet(cw, internalName);
    generateAsList(cw, internalName);
    generateIndexOf(cw, internalName, size);
    generateIterator(cw, internalName);

    cw.visitEnd();
    byte[] bytes = cw.toByteArray();

    try {
      java.nio.file.Files.write(java.nio.file.Paths.get("/tmp/Generated.class"), bytes);
    } catch (Exception e) {
      // ignore
    }

    try {
      return (Class<? extends NodeChildren>) new DefineClassHelper()
          .defineClass(className, bytes);
    }
    catch (Exception e) {
      throw new RuntimeException("Failed to define class: " + className, e);
    }
  }

  private static void generateClinit(ClassWriter pCw, String pInternalName, int pSize,
      List<IPropertyDescription> pDescriptions)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
    mv.visitCode();
    mv.visitIntInsn(SIPUSH, pSize);
    mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
    for (int i = 0; i < pSize; i++) {
      mv.visitInsn(DUP);
      pushInt(mv, i);
      mv.visitLdcInsn(pDescriptions.get(i).getName());
      mv.visitInsn(AASTORE);
    }
    mv.visitFieldInsn(PUTSTATIC, pInternalName, "fFieldNames", "[Ljava/lang/String;");
    mv.visitInsn(RETURN);
    mv.visitMaxs(3, 0);
    mv.visitEnd();
  }

  private static void generateConstructor(ClassWriter pCw, String pInternalName, int pSize,
      List<String> pFieldNames, List<IPropertyDescription> pDescriptions)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "<init>",
                        "([Lde/adito/propertly/core/api/INode;)V", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, "de/adito/propertly/core/api/AbstractNodeChildren",
                       "<init>", "()V", false);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitFieldInsn(PUTFIELD, pInternalName, "fNodes",
                      "[Lde/adito/propertly/core/api/INode;");
    for (int i = 0; i < pSize; i++) {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      pushInt(mv, i);
      mv.visitInsn(AALOAD);
      mv.visitFieldInsn(PUTFIELD, pInternalName, pFieldNames.get(i),
                        "Lde/adito/propertly/core/api/INode;");
    }
    mv.visitInsn(RETURN);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }

  private static void generateClear(ClassWriter pCw, String pInternalName, List<String> pFieldNames)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "clear", "()V", null, null);
    mv.visitCode();
    mv.visitIntInsn(SIPUSH, 0);
    mv.visitTypeInsn(ANEWARRAY, "de/adito/propertly/core/api/INode");
    mv.visitVarInsn(ASTORE, 1);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitFieldInsn(PUTFIELD, pInternalName, "fNodes",
                      "[Lde/adito/propertly/core/api/INode;");
    for (String fieldName : pFieldNames) {
      mv.visitVarInsn(ALOAD, 0);
      mv.visitInsn(ACONST_NULL);
      mv.visitFieldInsn(PUTFIELD, pInternalName, fieldName,
                        "Lde/adito/propertly/core/api/INode;");
    }
    mv.visitInsn(RETURN);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }

  private static void generateMutationMethods(ClassWriter pCw, String pInternalName)
  {
    generateUnsupportedOperationException(pCw, pInternalName, "add",
        "(Lde/adito/propertly/core/api/INode;)V");
    generateUnsupportedOperationException(pCw, pInternalName, "add",
        "(Ljava/lang/Integer;Lde/adito/propertly/core/api/INode;)V");
    generateUnsupportedOperationException(pCw, pInternalName, "remove",
        "(Lde/adito/propertly/core/api/INode;)Z");
    generateUnsupportedOperationException(pCw, pInternalName, "remove", "(I)V");
    generateUnsupportedOperationException(pCw, pInternalName, "rename",
        "(Lde/adito/propertly/core/api/IPropertyDescription;Ljava/lang/String;)V");
    generateUnsupportedOperationException(pCw, pInternalName, "reorder",
        "(Ljava/util/Comparator;)V");
  }

  private static void generateFindString(ClassWriter pCw, String pInternalName,
      List<IPropertyDescription> pDescriptions, List<String> pFieldNames)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "find",
        "(Ljava/lang/String;)Lde/adito/propertly/core/api/INode;", null, null);
    mv.visitCode();

    int size = pDescriptions.size();
    if (size == 0) {
      mv.visitInsn(ACONST_NULL);
      mv.visitInsn(ARETURN);
      mv.visitMaxs(1, 2);
      mv.visitEnd();
      return;
    }

    Label[] matchLabels = new Label[size];
    for (int i = 0; i < size; i++) {
      matchLabels[i] = new Label();
    }
    Label end = new Label();

    for (int i = 0; i < size; i++) {
      mv.visitFieldInsn(GETSTATIC, pInternalName, "fFieldNames", "[Ljava/lang/String;");
      pushInt(mv, i);
      mv.visitInsn(AALOAD);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals",
                          "(Ljava/lang/Object;)Z", false);
      mv.visitJumpInsn(IFNE, matchLabels[i]);
    }

    mv.visitLabel(end);
    mv.visitInsn(ACONST_NULL);
    mv.visitInsn(ARETURN);

    for (int i = 0; i < size; i++) {
      mv.visitLabel(matchLabels[i]);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, pInternalName, pFieldNames.get(i), "Lde/adito/propertly/core/api/INode;");
      mv.visitInsn(ARETURN);
    }

    mv.visitMaxs(1, 2);
    mv.visitEnd();
  }

  private static void generateFindDescription(ClassWriter pCw, String pInternalName,
      List<IPropertyDescription> pDescriptions, List<String> pFieldNames)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "find",
        "(Lde/adito/propertly/core/spi/IPropertyDescription;)"
            + "Lde/adito/propertly/core/api/INode;", null, null);
    mv.visitCode();

    int size = pDescriptions.size();
    if (size == 0) {
      mv.visitInsn(ACONST_NULL);
      mv.visitInsn(ARETURN);
      mv.visitMaxs(1, 3);
      mv.visitEnd();
      return;
    }

    Label[] matchLabels = new Label[size];
    for (int i = 0; i < size; i++) {
      matchLabels[i] = new Label();
    }
    Label end = new Label();

    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEINTERFACE, "de/adito/propertly/core/spi/IPropertyDescription",
                       "getName", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, 2);

    for (int i = 0; i < size; i++) {
      mv.visitVarInsn(ALOAD, 2);
      mv.visitLdcInsn(pDescriptions.get(i).getName());
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals",
                          "(Ljava/lang/Object;)Z", false);
      mv.visitJumpInsn(IFNE, matchLabels[i]);
    }

    mv.visitLabel(end);
    mv.visitInsn(ACONST_NULL);
    mv.visitInsn(ARETURN);

    for (int i = 0; i < size; i++) {
      mv.visitLabel(matchLabels[i]);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, pInternalName, pFieldNames.get(i), "Lde/adito/propertly/core/api/INode;");
      mv.visitInsn(ARETURN);
    }

    mv.visitMaxs(1, 3);
    mv.visitEnd();
  }

  private static void generateGet(ClassWriter pCw, String pInternalName)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "get", "(I)Lde/adito/propertly/core/api/INode;", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, pInternalName, "fNodes", "[Lde/adito/propertly/core/api/INode;");
    mv.visitVarInsn(ILOAD, 1);
    mv.visitInsn(AALOAD);
    mv.visitInsn(ARETURN);
    mv.visitMaxs(2, 2);
    mv.visitEnd();
  }

  private static void generateAsList(ClassWriter pCw, String pInternalName)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "asList", "()Ljava/util/List;", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, pInternalName, "fNodes", "[Lde/adito/propertly/core/api/INode;");
    mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "asList",
                       "([Ljava/lang/Object;)Ljava/util/List;", false);
    mv.visitMethodInsn(INVOKESTATIC, "java/util/Collections", "unmodifiableList",
                       "(Ljava/util/List;)Ljava/util/List;", false);
    mv.visitInsn(ARETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }

  private static void generateIndexOf(ClassWriter pCw, String pInternalName, int pSize)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "indexOf",
        "(Lde/adito/propertly/core/spi/IPropertyDescription;)I", null, null);
    mv.visitCode();

    if (pSize == 0) {
      mv.visitInsn(ICONST_M1);
      mv.visitInsn(IRETURN);
      mv.visitMaxs(1, 2);
      mv.visitEnd();
      return;
    }

    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEINTERFACE, "de/adito/propertly/core/spi/IPropertyDescription",
                       "getName", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, 2);
    mv.visitInsn(ICONST_M1);
    mv.visitVarInsn(ISTORE, 3);

    for (int i = 0; i < pSize; i++) {
      mv.visitFieldInsn(GETSTATIC, pInternalName, "fFieldNames", "[Ljava/lang/String;");
      pushInt(mv, i);
      mv.visitInsn(AALOAD);
      mv.visitVarInsn(ALOAD, 2);
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals",
                         "(Ljava/lang/Object;)Z", false);
      Label next = new Label();
      mv.visitJumpInsn(IFEQ, next);
      pushInt(mv, i);
      mv.visitVarInsn(ISTORE, 3);
      mv.visitLabel(next);
    }

    mv.visitVarInsn(ILOAD, 3);
    mv.visitInsn(IRETURN);
    mv.visitMaxs(2, 4);
    mv.visitEnd();
  }

  private static void generateIterator(ClassWriter pCw, String pInternalName)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, "iterator", "()Ljava/util/Iterator;", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, pInternalName, "asList", "()Ljava/util/List;", false);
    mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "iterator",
                       "()Ljava/util/Iterator;", true);
    mv.visitInsn(ARETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }

  private static void generateUnsupportedOperationException(ClassWriter pCw,
      String pInternalName, String pMethodName, String pDescriptor)
  {
    MethodVisitor mv = pCw.visitMethod(ACC_PUBLIC, pMethodName, pDescriptor, null, null);
    mv.visitCode();
    mv.visitTypeInsn(NEW, "java/lang/UnsupportedOperationException");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/UnsupportedOperationException",
                       "<init>", "()V", false);
    mv.visitInsn(ATHROW);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
  }

  private static void pushInt(MethodVisitor pMv, int pValue)
  {
    if (pValue == -1) {
      pMv.visitInsn(ICONST_M1);
    }
    else if (pValue >= 0 && pValue <= 5) {
      pMv.visitInsn(ICONST_0 + pValue);
    }
    else if (pValue >= -128 && pValue <= 127) {
      pMv.visitIntInsn(BIPUSH, pValue);
    }
    else {
      pMv.visitIntInsn(SIPUSH, pValue);
    }
  }

  private static final class DefineClassHelper extends ClassLoader
  {
    DefineClassHelper()
    {
      super(NodeChildrenGenerator.class.getClassLoader());
    }

    Class<?> defineClass(String pName, byte[] pBytes)
    {
      return super.defineClass(pName, pBytes, 0, pBytes.length);
    }
  }
}
