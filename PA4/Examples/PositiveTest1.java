/* Positive test files which check all possible statements,
variable declarations, and method calls between classes. */

import meggy.Meggy;

class PositiveTest1 {		//Main class
	public static void main(String[] whatever){
		int i;
		Meggy.setPixel((byte)3, (byte)10, Meggy.Color.GREEN);	//Meggy.setPixel(exp, exp, exp) statement
		Meggy.setAuxLEDs(123);									//Meggy.setAuxLEDs(exp) statement
		Meggy.toneStart(Meggy.Tone.As3, 400);					//Meggy.toneStart(exp,exp) statement
		{												//Block statement
			Meggy.delay(557);							//Meggy.delay(exp) statement
			if(true && false){				//'&&' operator
				{							//nested block statement
					if(((34 == 54))){		//nested if statement and '==' operator
					}
				}
			} else if(10 < 11){				//'<' operator
				while(true){				//while statement
					i = 1;
				}
			} else{
			}
		}
		Meggy.delay(1000);
	}
}

