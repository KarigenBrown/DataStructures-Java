package stack;

/*
 *逆波兰计算机（(3+4)*5-6 => 3 4 + 5 * 6 -）
 *1.从左至右扫描，将3和4压入栈
 *2.遇到+运算符，因此弹出4和3（4为栈顶元素，3为磁钉元素），计算出3+4的值，得7，再将7入栈
 *3.将5入栈。
 *4.接下来是*运算符，因此弹出5和7，计算出7*5=35，将35入栈。
 *5.将6入栈。
 *6.最后是-运算符，计算出35-6得值，即29，由此得出最终结果。
 *
 *中缀表达式转后缀表达式
 *1.初始化两个栈：运算符栈s1胡存储中间结果站s2
 *2.从左至右扫描中缀表达式
 *3.遇到操作数时，将其压入栈s2
 *4.遇到运算符时，比较其与s1栈顶运算符的优先级
 * 4.1 如果s1为空，或栈顶运算符为左括号"("，则直接将此运算符入栈
 * 4.2 否则，若优先级必栈顶元素的高，也将运算符压入s1
 * 4.3 否则，将s1栈顶的运算符弹出并压入到s2中，再次转到1. 与s1中新的栈顶运算符相比较//同级时栈里运算先发生（符合逻辑），所以同级也要pop，直到发现比当前符号优先级低的符号
 *5.遇到括号时
 * 5.1 如果是左括号"("，则直接压入s1
 * 5.2 如果是右括号")",则依次弹出s1栈顶的运算符，并压入s2，直到遇到左括号位置，此时将这一对括号丢弃重复步骤2至5，直到表达式的最右边
 *6.重复步骤2至5，直到表达式的最右边
 *7.将s1中剩余的运算符依次弹出并压入s2
 *8.依次弹出s2中的元素并输出，i俄国的逆序即为中缀表达式对应的后缀表达式
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

public class PolandNotation {
    public static void main(String[] args) {
	
	//将中缀表达式转化成后缀表达式的功能
	//说明
	//1. 1+((2+3)*4)-5) => 转成 1 2 3 + 4 * + 5 -
	//2.因为直接对字符串进行操作，不太方便，因此先将"1+((2+3)*4)-5)" => 中缀表达式对应的List
	//即"1+((2+3)*4)-5)" => ArrayList[ 1,+,(,(,2,+,3,),*,4,),-,5 ]
	//3.将得到的中缀表达式对应的List => 后缀表达式对应的List
	//即ArrayList[ 1,+,(,(,2,+,3,),*,4,),-,5 ] => ArrayList[ 1, 2, 3, +, 4, *, +, 5, - ]
	
	String expression="1+((2+3)*4)-5";
	List<String> infixExpressionList=toInfixExpression(expression);
	System.out.println("中缀表达式对应的List="+infixExpressionList);//ArrayList[ 1,+,(,(,2,+,3,),*,4,),-,5 ]
	List<String> suffixExpressionList=parseSuffixExpressionList(infixExpressionList);
	System.out.println("后缀表达式对应的List="+suffixExpressionList);//ArrayList[ 1, 2, 3, +, 4, *, +, 5, - ]
	
	System.out.printf("expression=%d",calculate(suffixExpressionList));
	/*
	
	//先定义一个逆波兰表达式
	//(3+4)*5-6 => 3 4 + 5 * 6 -
	//说明为了方便，逆波兰表达式的数字和符号使用空格隔开
	String suffixExpression="3 4 + 5 * 6 -";
	
	//思路
	//1.先将"3 4 + 5 * 6 -"放到ArrayList中
	//2.将ArrayList传递给一个方法，遍历ArrayList配合站完成计算
	List<String> rpnList=getListString(suffixExpression);
	System.out.println("rpnList="+rpnList);
	
	int res=calculate(rpnList);
	System.out.println("计算的结果是="+res);
	
	*/
    }
    
    //ArrayList[ 1,+,(,(,2,+,3,),*,4,),-,5 ] => ArrayList[ 1, 2, 3, +, 4, *, +, 5, - ]
    //方法：将得到的中缀表达式对应的List => 后缀表达式对应的List
    public static List<String> parseSuffixExpressionList(List<String> ls) {
	//定义两个栈
	Stack<String> s1=new Stack<String>();//符号栈
	//说明：s2这个栈，在整个转换过程中，没有pop操作，而且后面我们还需要逆序输出
	//因此比较麻烦，这里我们就不用Stack<String>直接使用List<String> s2
	//Stack<String> s2=new Stack<String>();//粗存中间结果的栈s2
	List<String> s2=new ArrayList<String>();//粗存中间结果的栈s2
	
	//遍历ls
	for (String item : ls) {
	    //如果是一个数， 加入s2
	    if (item.matches("\\d+")) {
		s2.add(item);
	    } else if (item.equals("(")) {
		s1.push(item);
	    } else if (item.equals(")")) {
		//如果是右括号")"，则依次弹出s1栈顶得运算符，直到遇到左括号为止，此时将这一对括号丢弃
		while (!s1.peek().equals("(")) {
		    s2.add(s1.pop());
		}
		s1.pop();//将 ( 弹出s1栈，消除小括号
	    } else {
		//当item的优先级小于等于s1栈顶运算符的优先级，将s1栈顶的运算符弹出并加入到s2中，item再次压入s1栈//再次转回开头与s1中新的栈顶运算符相比较
		//问题：我们缺少一个比较运算符优先级高低的方法
		while (s1.size()!=0&&Operation.getValue(s1.peek())>=Operation.getValue(item)) {
		    s2.add(s1.pop());
		}
		//还需要将item压入栈
		s1.push(item);
	    }
	}
	
	//将s1中剩余的运算符加入到s2中
	while (s1.size()!=0) {
	    s2.add(s1.pop());
	}
	return s2;//注意因为是存放到List中，因此按顺序输出就是对应的后缀表达式对应的List
    }
    
    //方法：将中缀表达式转成对应的List
    public static List<String> toInfixExpression(String s) {
	//先定义一个List，存放中缀表达式对应的内容
	List<String> ls=new ArrayList<String>();
	int i=0;//这是一个指针用于遍历中缀表达式字符串
	String str;//对多位数的拼接
	char c;//每遍历到一个字符就放入到c
	do {
	    //如果c是一个非数字，我需要加入到ls
	    if ((c=s.charAt(i))<'0'||(c=s.charAt(i))>'9') {
		ls.add(""+c);
		i++;
	    } else {//如果是一个属，需要考虑多位数的问题
		str="";//先将str置成"" '0'[48]->'9'[57]
		while (i<s.length()&&(c=s.charAt(i))>='0'&&(c=s.charAt(i))<='9') {
		    str+=c;//拼接
		    i++;
		}
		ls.add(str);
	    }
	} while (i<s.length());
	return ls;//返回
    }
    
    //将一个逆波兰表达式，依次将数和运算符放入到一个ArrayList中
    public static List<String> getListString(String suffixExpression) {
	//将suffixExpression分割
	String[] split=suffixExpression.split(" ");
	List<String> list=new ArrayList<String>();
	for (String ele : split) {
	    list.add(ele);
	}
	return list;
    }
    
    //完成对逆波兰表达式的计算
    //1.从左至右扫描，将3和4压入栈
    //2.遇到+运算符，因此弹出4和3（4为栈顶元素，3为磁钉元素），计算出3+4的值，得7，再将7入栈
    //3.将5入栈。接下来是*运算符，因此弹出5和7，计算出7*5=35，将35入栈。将6入栈。最后是-运算符，计算出35-6得值，即29，由此得出最终结果。
    public static int calculate(List<String> ls) {
	//创建栈，只需要一个栈即可
	Stack<String> stack=new Stack<String>();
	//遍历ls
	for (String item : ls) {
	    //这里使用正则表达式来取出数
	    if (item.matches("\\d+")) {//匹配的是多位数
		//入栈
		stack.push(item);
	    } else {
		//pop出两个数，并运算，再入栈
		int num2=Integer.parseInt(stack.pop());
		int num1=Integer.parseInt(stack.pop());
		int res=0;
		if (item.equals("+")) {
		    res=num1+num2;
		} else if (item.equals("-")) {
		    res=num1-num2;
		} else if (item.equals("*")) {
		    res=num1*num2;
		} else if (item.equals("/")) {
		    res=num1/num2;
		} else {
		    throw new RuntimeException("运算符有误");
		}
		//把res入栈
		stack.push(""+res);
	    }
	}
	//最后留在stack中的数据是运算结果
	return Integer.parseInt(stack.pop());
    }
}

//编写一个类Operation可以返回一个运算发对应的优先级
class Operation {
    private static int ADD=1;
    private static int SUB=1;
    private static int MUL=2;
    private static int DIV=2;
    
    //写一个方法，返回对应的优先级
    public static int getValue(String operation) {
	int result=0;
	switch(operation) {
	case "+":
	    result=ADD;
	    break;
	case "-":
	    result=SUB;
	    break;
	case "*":
	    result=MUL;
	    break;
	case "/":
	    result=DIV;
	    break;
	default:
	    System.out.println("不存在该运算符");
	    break;
	}
	return result;
    }
}
