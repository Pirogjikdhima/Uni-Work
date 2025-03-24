//public class neclass {
//    public static void main(String[] args){
//        String s= "hello";
//        String s1= "hello";
//
//        Object o = s;
//        if(o==s){
//            System.out.print("A ");
//        }
//        else{
//            System.out.print("B ");
//
//        }
////        if(o==s1){
////            System.out.print("A ");
////        }
////        else{
////            System.out.print("B ");
////
////        }
////        if(s1.equals(o)){
////            System.out.print("C ");
////        }
////        else{
////            System.out.print("D ");
////
////        }
//        if(s1==o){
//            System.out.print("C ");
//        }
//        else{
//            System.out.print("D ");
//
//        }
//
//    }
//}
class mammal{
    public mammal(int i){
        System.out.println("mAMAL");
    }
}
public class Platipusi {
    static int i  = 1;
    public Platipusi(){
        System.out.println("Plait");
    }
    public static void main(String[] args){
        String s = "";String t = "";
        for (int i = 0 ; i< 4 ; i++)
            s= s +"!";
        for (int i = 0 ; i< 4 ; i++)
            t=  t +"*";
        System.out.println(s);
        System.out.println(t);
    }
}