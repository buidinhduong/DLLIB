import java.util.Arrays;
import java.util.LinkedHashSet;


public class tets {
    public static void main(String[] are)
    {
        LinkedHashSet<Integer> a = new LinkedHashSet<Integer>();
        a.add(1);
        a.add(1);
        a.add(0);
        a.add(2);
        a.remove(1);
        a.add(1);
        System.out.println(Arrays.toString(a.toArray()));
        
        long ab = -3244332L;
         System.out.println(Integer.toBinaryString((int)(ab >> 32)));
         System.out.println("11111111111111111111111111111111".length());
         
         char c = '0';
         StringBuilder sb = new StringBuilder(c);
         sb.append("12");
         System.out.println(sb);
    }
}
