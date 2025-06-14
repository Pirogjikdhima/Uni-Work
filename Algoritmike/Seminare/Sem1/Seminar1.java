package Seminare.Sem1;

public class Seminar1 {
    //  Ushtrimi 12
    public static void Printo2Vektoret(int[] v1, int[] v2) {

        int N = v1.length;
        int M = v2.length;
        int j = 0,i = 0;
        while (j<M && i < N)
        {
            if (v1[i]<v2[j])
                i++;
            else if (v1[i]>v2[j])
                j++;
            else {
                System.out.println(v1[i]);
                i++;
                j++;
            }
        }
    }

    public static void main(String[] args) {
        int[] vektori1 = {1,2,3,4,6,7,8};
        int[] vektori2 = {0,1,2,3,4,5,6,7,8};
        Printo2Vektoret(vektori1,vektori2);
    }
}