package com.cogician.quicker.struct;

import java.util.Arrays;

/**
 * <p>
 * Test this package.
 * </p>
 *
 * @author Fred Suvn
 * @version 0.0.0, 2016-04-20T11:37:45+08:00
 * @since 0.0.0, 2016-04-20T11:37:45+08:00
 */
public class TestStruct {

    public static void main(String[] args) {
        testLinked();
        testTree();
        testBiTree();
        testSwitch();
    }

    private static void testLinked() {
        QuickLinkedNode<String> root = new QuickLinkedNode<>("root");
        root.linkNextByValue("1").linkNextByValue("2").linkNextByValues(Arrays.asList("3", "4", "5"))
                .linkNextByValue("6").setNext(root);
        root.linkPreviousByValue("-1").linkPreviousByValue("-2").linkPreviousByValue("-3").linkPreviousByValue("-4")
                .linkPreviousByValue("-5").linkPreviousByValue("-6").setPrevious(root);
        System.out.println("isNextCircular: " + root.isNextCircular());
        System.out.println("isPreviousCircular: " + root.isPreviousCircular());
        System.out.println("Next : ");
        root.flowNext().each(n -> System.out.println(n));
        System.out.println("Previous : ");
        root.flowPrevious().each(n -> System.out.println(n));
    }

    private static void testTree() {
        QuickTreeNode<String> root = new QuickTreeNode<String>("A");
        root.childrenValues().addAll(Arrays.asList("B", "C"));
        root.bidirectionalLink();
        root.children().get(0).childrenValues().addAll(Arrays.asList("D", "F"));
        root.children().get(0).children().get(0).childrenValues().add("E");
        root.children().get(0).children().get(1).childrenValues().add("G");
        root.children().get(0).bidirectionalLink();
        root.children().get(0).children().get(0).bidirectionalLink();
        root.children().get(0).children().get(1).bidirectionalLink();
        System.out.println("Preorder: (abdefgc)");
        root.flowPreorder().each(n -> System.out.println(n));
        System.out.println("Postorder: (edgfbca)");
        root.flowPostorder().each(n -> System.out.println(n));
        System.out.println("Levelorder: (abcdfeg)");
        root.flowLevelorder().each(n -> System.out.println(n));
        System.out.println("Level: ");
        root.flowLevel().each(l -> System.out.println(l));
    }

    private static void testBiTree() {
        QuickBiTreeNode<String> root = new QuickBiTreeNode<String>("A");
        root.linkLeftByValue("B").linkLeftByValue("D").linkRightByValue("E");
        root.getLeft().linkRightByValue("F").linkLeftByValue("G");
        root.linkRightByValue("C");
        System.out.println("Preorder: (abdefgc)");
        root.flowPreorder().each(n -> System.out.println(n));
        System.out.println("Inorder: (debgfac)");
        root.flowInorder().each(n -> System.out.println(n));
        System.out.println("Postorder: (edgfbca)");
        root.flowPostorder().each(n -> System.out.println(n));
        System.out.println("Levelorder: (abcdfeg)");
        root.flowLevelorder().each(n -> System.out.println(n));
        System.out.println("Level: ");
        root.flowLevel().each(n -> System.out.println(n));
    }

    private static void testSwitch() {
        QuickSwitch<Integer> s = new QuickSwitch<>(Arrays.asList(new QuickCase<>(i -> i == 1, e -> {
            System.out.println("this is 1.");
        }), new QuickCase<>(i -> i == 2, e -> {
            System.out.println("this is 2.");
        }), new QuickCase<>(i -> i == 3, e -> {
            System.out.println("this is 3.");
        })), new QuickCase<>(null, e -> {
            System.out.println("this is a number.");
        }));
        s.perform(2);
        System.out.println("Switch statement: ");
        switch (2) {
            case 1:
                System.out.println("this is 1.");
                break;
            case 2:
                System.out.println("this is 2.");
                break;
            case 3:
                System.out.println("this is 3.");
                break;
            default:
                System.out.println("this is a number.");
        }
    }
}
