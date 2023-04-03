package mobhsap.util;

import java.nio.charset.Charset;
import java.util.Stack;

public class StringUtils {

  public static boolean isEmpty(String s) {
    if (s == null) return true;
    return s.trim().equals("");
  }

  public static String stringSql(String s) {
    if (s != null) {
      return s.replaceAll("'", "''");
    }
    return null;
  }

  public static String sanitize(String string) {
    int countSpace = 0;
    String stringCleaned = "";

    for (int i = 0; i < string.length(); i++) {
      Character character = string.charAt(i);

      if (Charset.forName("US-ASCII").newEncoder().canEncode(character)) {
        String characterCleaned =
          (character + "").replaceAll(
              "[+,'\"!@#$&\\*()\\[\\]\\-/\\\\:;\\.\\?|]",
              ""
            );

        if (characterCleaned.isEmpty()) continue;

        if (Character.isWhitespace(character)) {
          countSpace++;
        } else {
          countSpace = 0;
        }

        if (countSpace <= 1) {
          stringCleaned += character + "";
        }
      }
    }
    stringCleaned = stringCleaned.trim();

    return stringCleaned;
  }

	public static boolean isAnyValue(String cat) {
		String s = cat.replaceAll("\\(", "");
		s = s.replaceAll("\\)", "");
		return s.equals(".*") || s.equals(".") || s.equals(".+");
	}

	public static boolean isPlusExpression(String text) {
		if(text.charAt(text.length()-1) == '+') {
			if (isBalanced(text))
				return true;
		}
		return false;

//		if(text.startsWith("(") && text.endsWith(")+")) {
//			return true;
//		}
//		return false;
	}

	public static boolean isPlusAnyExpression(String text) {
		if(text.charAt(text.length()-1) == '*') {
			if (isBalanced(text))
				return true;
		}
		return false;
		
	
//		if(finishWithStar(text))
//			return false;
//
//		if(text.startsWith("(") && text.endsWith(")*")) {
//			return true;
//		}
//		return false;

	}
	
	private static boolean finishWithStar(String text) {
		return text.endsWith("(( \\w*))*");
	}
	
	public static void main(String[] args) {
//		String s = " ( torridipisa)(;(\\w* ) )*";
		String s = " ((\\w*))*";
		s = s.trim();								//tirando os espaços vazios do inicio e fim
		s = s.substring(0, s.length()-1).trim(); 	//removendo o + ou *
		s = s.substring(1).trim(); 					//removendo o 1º (
		s = s.substring(0, s.length()-1); 			//removendo o último )
		
		System.out.println(isBalanced(s));
		
//		Stack<String> stack = new Stack<>();
//		stack.push(null)
	}
	
	public static boolean isBalanced(String text) {
		String s = text;
		s = s.trim();								//tirando os espaços vazios do inicio e fim
		s = s.substring(0, s.length()-1).trim(); 	//removendo o + ou *
		s = s.substring(1).trim(); 					//removendo o 1º (
		s = s.substring(0, s.length()-1); 			//removendo o último )

		int count = 0;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '(') {
				count++;
				
			} else if (c == ')') {
				count--;
				if(count < 0) {
					return false;
				}
			}
		}
		if(count < 0 || count > 0)
			return false;
		return true;
	}
}