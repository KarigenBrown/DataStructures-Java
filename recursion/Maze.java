package recursion;

/*
 * 迷宫问题说明
 * 1.小球得到的路径，和程序员设置的找路策略有关，即：找路的上下左右的顺序相关
 * 2.再得到小球路径时，可以先使用（下右上左），再改成（上右下左），看看路径是不是右变化
 * 3.测试回溯现象
 */

public class Maze {

    public static void main(String[] args) {
	//先创建一个二维数组模拟迷宫
	//地图
	int[][] map=new int[8][7];
	//使用1表示墙
	//上下全部置为1
	for (int i = 0; i < 7; i++) {
	    map[0][i]=1;
	    map[7][i]=1;
	}
	
	//左右全部置为1
	for (int i = 0; i < 8; i++) {
	    map[i][0]=1;
	    map[i][6]=1;
	}
	
	//设置挡板
	map[3][1]=1;
	map[3][2]=1;
	//map[1][2]=1;
	//map[2][2]=1;
	
	//输出地图
	System.out.println("地图的情况：");
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 7; j++) {
		System.out.print(map[i][j]+" ");
	    }
	    System.out.println();
	}
	
	//使用递归回溯给小球找路
	//setWay(map, 1, 1);
	setWay2(map, 1, 1);
	
	//输出新的地图，小球走过，并标识过的递归
	System.out.println("小球走过，并标识过的地图的情况：");
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 7; j++) {
		System.out.print(map[i][j]+" ");
	    }
	    System.out.println();
	}
	
	//
    }
    
    //使用递归回溯来给小球找路
    //说明
    //1.map 表示地图
    //2.i、j，表示从地图哪个位置开始出发（1，1）
    //3.如果小球能到map[6][5]位置，则说明通路找到
    //4.约定：当map[i][j]为0表示该点没有走过当为1表示墙，2表示通路可以走，3表示该点已经走过，但是走不通
    //5.在走迷宫时，选要确定一个策略（方法），下->右->上->左,如果该点走不通，再回溯。
    /**
     * 
     * @param map 表示地图
     * @param i 从哪个位置开始找
     * @param j
     * @return 如果找到通路就返回true，否者返回false
     */
    public static boolean setWay(int[][] map, int i, int j) {
	if (map[6][5]==2) {//通路已经找到
	    return true;
	} else {
	    if (map[i][j]==0) {//如果当前这个点还没有走过
		//按照策略 下->右->上->左 走
		map[i][j]=2;//假定该点时可以走通
		if (setWay(map, i+1, j)) {//向下走
		    return true;
		} else if (setWay(map, i, j+1)) {//向右走
		    return true;
		} else if (setWay(map, i-1, j)) {//向上走
		    return true;
		} else if (setWay(map, i, j-1)) {//向左走
		    return true;
		} else {
		    //说明该点是走不通，是死路
		    map[i][j]=3;
		    return false;
		}
	    } else {//如果map[i][j]!=0,可能是1，2，3
		return false;
	    }
	}
    }
    
    //修改找路的策略，改成 上->右->下->左
    public static boolean setWay2(int[][] map, int i, int j) {
	if (map[6][5]==2) {//通路已经找到
	    return true;
	} else {
	    if (map[i][j]==0) {//如果当前这个点还没有走过
		//按照策略 下->右->上->左 走
		map[i][j]=2;//假定该点时可以走通
		if (setWay2(map, i-1, j)) {//向上走
		    return true;
		} else if (setWay2(map, i, j+1)) {//向右走
		    return true;
		} else if (setWay2(map, i+1, j)) {//向下走
		    return true;
		} else if (setWay2(map, i, j-1)) {//向左走
		    return true;
		} else {
		    //说明该点是走不通，是死路
		    map[i][j]=3;
		    return false;
		}
	    } else {//如果map[i][j]!=0,可能是1，2，3
		return false;
	    }
	}
    }
}