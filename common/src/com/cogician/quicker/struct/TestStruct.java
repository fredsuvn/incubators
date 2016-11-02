package com.cogician.quicker.struct;

import com.cogician.quicker.Quicker;

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
        LinkedNode<String> root = new LinkedNode<>("root");
        root.linkNextByValue("1").linkNextByValue("2").linkNextsByValues("3", "4", "5").linkNextByValue("6")
                .setNext(root);
        root.linkPreviousByValue("-1").linkPreviousByValue("-2").linkPreviousByValue("-3").linkPreviousByValue("-4")
                .linkPreviousByValue("-5").linkPreviousByValue("-6").setPrevious(root);
        System.out.println("isNextCircular: " + root.isNextCircular());
        System.out.println("isPreviousCircular: " + root.isPreviousCircular());
        System.out.println("Next iterator: ");
        Quicker.each(root.iteratorInNext(), e -> {
            System.out.println(e);
        });
        System.out.println("Previous iterator: ");
        Quicker.each(root.iteratorInPrevious(), e -> {
            System.out.println(e);
        });
    }

    private static void testTree() {
        TreeNode<String> root = new TreeNode<String>("A");
        root.linkChildByValue("B");
        root.getChild().linkChildrenByValues("C", "D");
        root.getChild().getChild(1).linkChildrenByValues("E", "F");
        root.getChild().getChild(1).getChild(0).linkChildByValue("G");
        System.out.println("Preorder: (ABCDEGF)");
        Quicker.each(root.iteratorInPreorder(), e -> {
            System.out.println(e);
        });
        System.out.println("Postorder: (CGEFDBA)");
        Quicker.each(root.iteratorInPostorder(), e -> {
            System.out.println(e);
        });
        System.out.println("Levelorder: (ABCDEFG)");
        Quicker.each(root.iteratorInLevelorder(), e -> {
            System.out.println(e);
        });
        System.out.println("Level: ");
        Quicker.each(root.levelIterator(), e -> {
            System.out.println(e);
        });
    }

    private static void testBiTree() {
        BiTreeNode<String> root = new BiTreeNode<String>("A");
        root.linkLeftByValue("B").linkLeftByValue("C").linkBrotherByValue("D").linkLeftByValue("E")
                .linkBrotherByValue("F").getBrother().linkRightByValue("G");
        System.out.println("Preorder: (ABCDEGF)");
        Quicker.each(root.iteratorInPreorder(), e -> {
            System.out.println(e);
        });
        System.out.println("Inorder: (CBEGDFA)");
        Quicker.each(root.iteratorInInorder(), e -> {
            System.out.println(e);
        });
        System.out.println("Postorder: (CGEFDBA)");
        Quicker.each(root.iteratorInPostorder(), e -> {
            System.out.println(e);
        });
        System.out.println("Levelorder: (ABCDEFG)");
        Quicker.each(root.iteratorInLevelorder(), e -> {
            System.out.println(e);
        });
        System.out.println("Level: ");
        Quicker.each(root.levelIterator(), e -> {
            System.out.println(e);
        });
    }

    private static void testSwitch() {
        Switch<Integer> s = new SwitchBuilder<Integer>().addCase(new Case<Integer>(1, i -> {
            System.out.println("this is 1.");
        })).addCase(new Case<Integer>(2, i -> {
            System.out.println("this is 2.");
        })).addCase(new Case<Integer>(3, i -> {
            System.out.println("this is 3.");
        })).setDefaultCase(new Case<Integer>(null, i -> {
            System.out.println("this is a number.");
        })).build();
        s.perform(Switch.CYCLE, 2);
        s.perform(Switch.CYCLE, 1);
        s.perform(Switch.CYCLE, 4);
        s.perform(Switch.CYCLE, 3);
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
