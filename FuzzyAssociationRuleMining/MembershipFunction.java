package LIRMM;

import org.junit.Test;

public class MembershipFunction {
  private static float minOf(float a,float b,float c)
  {
      float minab=Math.min(a, b);
      return Math.min(c, minab);
  }
  @Test
  public void test()
  {
      try {
      float x=(float)4/5;
      float a=(float)0.6;
      float b=(float)0.75;
      float c=(float)0.8;
      float d=(float)1;
      
      float degree=MembershipFunction.TrapezoidalMFs(x,a,b , c, d);
      System.out.print("x:"+x+" a "+a+" defree  "+degree);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
  public static float TrapezoidalMFs(float x,float abcd[]) throws Exception{
      return TrapezoidalMFs(x, abcd[0],abcd[1],abcd[2],abcd[3]);
  }
  public static float TrapezoidalMFs(int x,int abcd[]) throws Exception{
      return TrapezoidalMFs(x, abcd[0],abcd[1],abcd[2],abcd[3]);
  }
   public static float TrapezoidalMFs(float x,float a,float b,float c,float d) throws Exception
   {
       //true condition a < b <= c < d
       if(!(a<b&&b<=c&&c<d)){
           throw new Exception("Paramerters is invalid: a < b <= c < d");}
       return Math.max(minOf((x-a)/(b-a), 1, (d-x)/(d-c)), 0);
       
   }
   public static float TriangularMFs(float x,float a,float b,float c)
   {
       //true condition a, b, c
      return Math.max(Math.min((x-a)/(b-a), (c-x)/(c-b)), 0);
   }
   public float GaussianMFs(float value) throws Exception
   {
       throw new Exception("Not implemented");

   }
   public float GeneralisedBellMFs(float value) throws Exception
   {
       throw new Exception("Not implemented");

   }
   public float SigmoidMFs(float value) throws Exception
   {
       throw new Exception("Not implemented");

   }
   public float  LeftRightMF(float value) throws Exception
   {
       throw new Exception("Not implemented");

   }
  
}
