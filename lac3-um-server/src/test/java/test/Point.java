package test;

import java.io.Serializable;

public class Point implements Serializable{
    private static final long serialVersionUID = 9111885154907080631L;
    private Double x;  
        private Double y;  
        public Point (Double x , Double y) {  
            this.x = x;  
            this.y = y;  
        }  
        public Double getX() {  
            return x;  
        }  
        public void setX(Double x) {  
            this.x = x;  
        }  
        public Double getY() {  
            return y;  
        }  
        public void setY(Double y) {  
            this.y = y;  
        }      
}