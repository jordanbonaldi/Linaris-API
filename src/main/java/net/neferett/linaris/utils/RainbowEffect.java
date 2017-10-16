package net.neferett.linaris.utils;

public class RainbowEffect {

	String message;
	String color;
	String rainbowColor;
	int position;
	int waitTime;
	
	public RainbowEffect(String message,String color,String rainbowColor,int waitTime) {
		this.message = message;
		this.color = color;
		this.rainbowColor = rainbowColor;
		this.position = 0;
		this.waitTime = waitTime;
	}
	
	public String next() {
		
		String returned = message;
		
		StringBuilder sb = new StringBuilder("");
	
		
		if (position > returned.length()+waitTime) {
			position = 0;
		}
		
		
		for (int pos = 0 ; pos < returned.length() ; pos++) {
			
			char c = returned.charAt(pos);
			
			if (pos == position) {
				sb.append(getRainbowColor() + c);
			} else {
				if (pos-position == 1 || pos==0)
					sb.append(getColor() + c);
				else 
					sb.append(c);
			} 
			
		}
		
		
		position++;
		
		return sb.toString();
	}
	
	public String getRainbowColor() {
		return rainbowColor;
	}
	
	public String getColor() {
		return color;
	}
}
