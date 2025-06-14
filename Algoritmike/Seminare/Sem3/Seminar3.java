package Seminare.Sem3;

public class Seminar3 {
//    Ushtrimi 13
    public static int KendallDistance(int[] v1,int[] v2){
        int distance = 0;

        int[] helper = new int[v1.length];
        for (int i = 0;i < helper.length;i++)
            helper[v2[i]] = i;


        for (int i = 0; i < v1.length; i++) {
            for (int j = i + 1; j < v2.length; j++) {
                if ((helper[v1[i]] > helper[v1[j]])) {
                    distance++;
                }
            }
        }
        return distance;

    }
    public static void main(String[] args) {
        int[] perm1 = {0, 3, 1, 6, 2, 5, 4};
        int[] perm2 = {1, 0, 3, 6, 4, 2, 5};

        int distance = KendallDistance(perm1, perm2);
        System.out.println("Distanca Kendall tau (O(n^2)): " + distance);
    }

}
