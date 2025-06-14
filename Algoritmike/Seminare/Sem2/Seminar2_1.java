package Seminare.Sem2;

import java.io.IOException;

public class Seminar2_1 {
    //    Ushtrimi 2
    //    a)
    public static void PrintoTek(int n ){
        if(n<1)
            return;

        if(n%2 ==1) {
            PrintoTek(n - 2);
            System.out.print(n+" ");
        }
        else {
            PrintoTek(n - 1);
        }
    }
    //    b)
    public static void PrintoTek2(int n ){
        if(n<1)
            return;

        if(n%2 ==1) {
            System.out.print(n+" ");
            PrintoTek2(n - 2);
        }
        else {
            PrintoTek2(n - 1);
        }
    }
    //    Ushtrimi 3
    public static void Sirakuza(int n){
        if (n<2) {
            System.out.print(n + " ");
            return;
        }
        System.out.print(n+" ");

        if (n%2==0){
            Sirakuza(n/2);
        }
        else{
            Sirakuza((3*n + 1));
        }

    }
    //    Ushtrimi 4
    public static int Shumezimi(int a, int b) {

        if (b < 0) {
            return -Shumezimi(a, -b);
        }
        if (b == 0) {
            return 0;
        }
        return a + Shumezimi(a, b - 1);
    }
    //    Ushtrimi 5
    public static int PMP(int n,int m){
        if(m<=n && n%m==0){
            return m;
        }
        else if (n<m)
            return PMP(m,n);
        else
            return PMP(m,n%m);
    }
    //    Ushtrimi 6
    public static void cubes(int n){
        if (n<1){
            return;
        }
        cubes(n-1);
        System.out.print(n*n*n + " ");
    }

    public static void main(String[] args) throws IOException {
//        PrintoTek(9);
//        System.out.println();
//        PrintoTek2(9);
//        Sirakuza(101);
//        System.out.println(Shumezimi(-4,-2));
//        System.out.println(PMP(6,3));
//        cubes(4);

    }
}
