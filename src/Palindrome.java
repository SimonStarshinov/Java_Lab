import jdk.dynalink.beans.StaticClass;

public class Palindrome {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            System.out.print(isPalindrome(s) + " ");

        }
    }

    public static String reverseString(String orig_string) {
        String reverse_string = "";
        for (int i = orig_string.length()-1; i > -1; i--){
            reverse_string += orig_string.charAt(i);
        }
        return reverse_string;
    }

    public static boolean isPalindrome(String s){
        String orig_string = s;
        String reverse_string = reverseString(orig_string);
        return orig_string.equals(reverse_string);
    }

}

