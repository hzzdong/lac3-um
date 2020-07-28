package test;

public class Test01 {
    
     public static void main(String[] args) {  
         long start =System.currentTimeMillis();
             //114.331951,30.64091#114.341049,30.610185#114.331436,30.588058#114.312038,30.56393#114.293498,30.558609#114.267922,30.563784#114.231185,30.57945#114.212303,30.601616#114.235649,30.626878#114.280624,30.646818#
         Point[] ps = new Point[] { new Point(114.309914,30.599556),//114.309914,30.599556
                    new Point(114.295688,30.592879),//114.295688,30.592879
                    new Point(114.292812,30.587726), //114.292812,30.587726
                    new Point(114.292812,30.587726), //114.292812,30.587726
                    new Point(114.30058,30.580318),//114.30058,30.580318
                    new Point(114.303606,30.586959),//114.303606,30.586959
                    new Point(114.304534,30.594751),//114.304534,30.594751
                    new Point(114.30838,30.590131),//114.30838,30.590131
                    new Point(114.308651,30.584182),//114.308651,30.584182
                    new Point(114.304495,30.584015),//114.304495,30.584015
                    new Point(114.301301,30.578759),//114.301301,30.578759
                    new Point(114.309437,30.578528),//114.309437,30.578528
                    new Point(114.323282,30.592786)};//114.323282,30.592786
            Point n1 = new Point(114.303217,30.583553);  
            Point n2 = new Point(114.307336,30.597592);  
            Point n3 = new Point(114.286565,30.590056);  
            Point y1 = new Point(114.227342,30.587987);  
            Point y2 = new Point(120.1866 , 30.2672);  
            Point y4 = new Point(120.1869 , 30.2718);  
            
//            long start =System.currentTimeMillis();
            System.out.println( "n1:" + isPtInPoly(n1.getX() , n1.getY() , ps));  
            System.out.println( "n2:" + isPtInPoly(n2.getX() , n2.getY() , ps));  
            System.out.println( "n3:" + isPtInPoly(n3.getX() , n3.getY() , ps));  
            System.out.println( "y1:" + isPtInPoly(y1.getX() , y1.getY() , ps));  
            System.out.println( "y2:" + isPtInPoly(y2.getX() , y2.getY() , ps));  
            System.out.println( "y4:" + isPtInPoly(y4.getX() , y4.getY() , ps));  
            System.out.println(System.currentTimeMillis()-start);
        }  
        public static boolean isPtInPoly (double ALon , double ALat , Point[] ps) {  
            int iSum, iCount, iIndex;  
            double dLon1 = 0, dLon2 = 0, dLat1 = 0, dLat2 = 0, dLon;  
            if (ps.length < 3) {  
                return false;  
            }  
            iSum = 0;  
            iCount = ps.length;  
            for (iIndex = 0; iIndex<iCount;iIndex++) {  
                if (iIndex == iCount - 1) {  
                    dLon1 = ps[iIndex].getX();  
                    dLat1 = ps[iIndex].getY();  
                    dLon2 = ps[0].getX();  
                    dLat2 = ps[0].getY();  
                } else {  
                    dLon1 = ps[iIndex].getX();  
                    dLat1 = ps[iIndex].getY();  
                    dLon2 = ps[iIndex + 1].getX();  
                    dLat2 = ps[iIndex + 1].getY();  
                }  
                // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上  
                if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1))) {  
                    if (Math.abs(dLat1 - dLat2) > 0) {  
                        //得到 A点向左射线与边的交点的x坐标：  
                        dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat) ) / (dLat1 - dLat2);  
                        // 如果交点在A点左侧（说明是做射线与 边的交点），则射线与边的全部交点数加一：  
                        if (dLon < ALon) {  
                            iSum++;  
                        }  
                    }  
                }  
            }  
            if ((iSum % 2) != 0) {  
                return true;  
            }  
            return false;  
        }  
    }