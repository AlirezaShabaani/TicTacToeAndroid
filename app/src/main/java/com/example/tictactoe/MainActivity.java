package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img0,img1,img2,img3,img4,img5,img6,img7,img8;

    static public final int MULTIPLY = 1;
    static public final int CIRCLE = 2;
    static public final int NOT_PLAYED = 0;
    static public final int DRAW = 3;
//    List<String> PrintDepth = new ArrayList<>();
    List<States> availables = new ArrayList<States>();
    List<ScoresOfStates> scoresOfStates = new ArrayList<>();

    int Turn = MULTIPLY;
    int winner = DRAW;

    int winningPositions[][] = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    int diagonalCheck[][] = {{0,4,8},{2,4,6}};
    int columns[][] = {{0,3,6},{1,4,7},{2,5,8}};
    int rows[][]={{0,1,2},{3,4,5},{6,7,8}};
    public int board[] = {NOT_PLAYED,NOT_PLAYED,NOT_PLAYED,NOT_PLAYED,NOT_PLAYED,NOT_PLAYED,NOT_PLAYED,NOT_PLAYED,NOT_PLAYED};
    public int board2[][] = new int[3][3];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img0 = findViewById(R.id.img);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);

        img0.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        img7.setOnClickListener(this);
        img8.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ImageView img = (ImageView) view;
        int tag = Integer.parseInt((String) view.getTag());

        if(winner != DRAW || board[tag] != NOT_PLAYED){
            return;
        }
        if(Turn == MULTIPLY){
            img.setImageResource(R.drawable.multiply);
            board[tag] = Turn;
            equalMaker(tag);
            Turn = CIRCLE;
            img.animate().alpha(0.8f).setDuration(500);
        }
        if(!EndedDraw() || winner != DRAW){
            alphaBetaMinimax(Integer.MIN_VALUE,Integer.MAX_VALUE,0,CIRCLE);
            for(ScoresOfStates sos : scoresOfStates){
                System.out.println(sos.score + ": score  for  state : "+sos.state);
            }
            States n = returnBestMove();
            int m = re_equalMaker(n);
            GridLayout gridLayout = findViewById(R.id.gridlayout);
            ImageView imageView = gridLayout.getChildAt(m) instanceof ImageView ? (ImageView)gridLayout.getChildAt(m) : null;
            imageView.setImageResource(R.drawable.circle);
            imageView.animate().alpha(0.8f).setStartDelay(1000).setDuration(1000);
            board[m] = CIRCLE;
            equalMaker(m);
            Turn = MULTIPLY;
        }
        Declare();
    }

    private Integer re_equalMaker(States n) {
        if (n.a == 0 && n.b == 0) { return 0; }
        else if(n.a == 0 && n.b == 1){return 1;}
        else if(n.a == 0 && n.b == 2){return 2;}
        else if(n.a == 1 && n.b == 0){return 3;}
        else if(n.a == 1 && n.b == 1){return 4;}
        else if(n.a == 1 && n.b == 2){return 5;}
        else if(n.a == 2 && n.b == 0){return 6;}
        else if(n.a == 2 && n.b == 1){return 7;}
        else if(n.a == 2 && n.b == 2){return 8;}
        return null;
    }

    private void equalMaker(int tag) {
        switch (tag){
            case 0:
                board2[0][0] = Turn;
                break;
            case 1:
                board2[0][1] = Turn;
                break;
            case 2:
                board2[0][2] = Turn;
                break;
            case 3:
                board2[1][0] = Turn;
                break;
            case 4:
                board2[1][1] = Turn;
                break;
            case 5:
                board2[1][2] = Turn;
                break;
            case 6:
                board2[2][0] = Turn;
                break;
            case 7:
                board2[2][1] = Turn;
                break;
            case 8:
                board2[2][2] = Turn;
                break;
        }
    }


    private boolean EndedDraw(){

        for(int i = 0 ; i < board.length ; i++){
            if(board[i] == NOT_PLAYED){
                return false;
            }
        }

        return true;
    }

    private  void Declare(){
        winner = Check();
        if(winner == MULTIPLY){
            Toast.makeText(MainActivity.this,"Multiply Won",Toast.LENGTH_SHORT).show();
        }
        else if(winner == CIRCLE){
            Toast.makeText(MainActivity.this,"Circle Won",Toast.LENGTH_SHORT).show();
        }else if ( EndedDraw()){
            if (winner == DRAW) {
                Toast.makeText(MainActivity.this,"Draw !!",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public int Check(){
        for(int[] status : winningPositions){
            if(board[status[0]] == board[status[1]] && board [status[1]] == board[status[2]] ){
                if(board[status[0]] != NOT_PLAYED){
                    return board[status[0]];
                }
            }
        }
        return DRAW;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Reset");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Reset();
        return super.onOptionsItemSelected(item);
    }

    public void Reset(){
        for(int i = 0 ; i < board.length ; i++){
            board[i] = NOT_PLAYED;
        }
        Turn = MULTIPLY;
        winner = DRAW;

        GridLayout gridLayout = findViewById(R.id.gridlayout);
        for(int i = 0 ; i < gridLayout.getChildCount() ; i++){
            ImageView imageView = (gridLayout.getChildAt(i) instanceof ImageView) ? (ImageView) gridLayout.getChildAt(i) : null;
            if(gridLayout == null) return;
            imageView.setImageResource(0);
        }
    }

    //==================================================================
        int aa = 0;
//    public List<Integer> getAvailables(){
//        List<Integer> list = new ArrayList<>();
//        for(int i = 0 ; i < this.board.length; i++){
//            if(board[i] == NOT_PLAYED){
//                list.add(i);
//            }
//        }
//        return list;
//    }

//    public int bestMove(){
//        int best = -1000;
//        int val = -1;
//
//        for (int i = 0 ; i < scoresOfStates.size() ; i++){
//            if(best < scoresOfStates.get(i).score){
//                best = scoresOfStates.get(i).score;
//                val = i;
//            }
//        }
//        return scoresOfStates.get(val).state;
//    }

    int uptoDepth = -1;
    public int alphaBetaMinimax(int alpha, int beta, int depth, int turn){

        if(beta<=alpha){
            System.out.println("Pruning at depth = "+depth);
                if(turn == 1) return Integer.MAX_VALUE; else return Integer.MIN_VALUE;
        }

        if(depth == uptoDepth || isGameOver()) return evaluateBoard();

        List<States> pointsAvailable = getAvailableStates();

        if(pointsAvailable.isEmpty()) return 0;

        if(depth==0) scoresOfStates.clear();

        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;

        for(int i=0;i<pointsAvailable.size(); ++i){
            States states = pointsAvailable.get(i);

            int currentScore = 0;

            if(turn == CIRCLE){
                placeAMove(states, CIRCLE);
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 2);
                maxValue = Math.max(maxValue, currentScore);

                //Set alpha
                alpha = Math.max(currentScore, alpha);

                if(depth == 0)
                    scoresOfStates.add(new ScoresOfStates(currentScore, states));
            }else if(turn == MULTIPLY){
                placeAMove(states, MULTIPLY);
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 1);
                minValue = Math.min(minValue, currentScore);

                //Set beta
                beta = Math.min(currentScore, beta);
            }
            //reset board
            board2[states.a][states.b] = 0;

            //If a pruning has been done, don't evaluate the rest of the sibling states
            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
        }
        return turn == CIRCLE ? maxValue : minValue;
    }

    public int evaluateBoard() {
        int score = 0;

        //Check all rows
        for (int i = 0; i < 3; ++i) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int j = 0; j < 3; ++j) {
                if (board2[i][j] == NOT_PLAYED) {
                    blank++;
                } else if (board2[i][j] == CIRCLE) {
                    X++;
                } else {
                    O++;
                }

            }
            score+=changeInScore(X, O);
        }

        //Check all columns
        for (int j = 0; j < 3; ++j) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int i = 0; i < 3; ++i) {
                if (board2[i][j] == NOT_PLAYED) {
                    blank++;
                } else if (board2[i][j] == CIRCLE) {
                    X++;
                } else {
                    O++;
                }
            }
            score+=changeInScore(X, O);
        }

        int blank = 0;
        int X = 0;
        int O = 0;

        //Check diagonal (first)
        for (int i = 0, j = 0; i < 3; ++i, ++j) {
            if (board2[i][j] == CIRCLE) {
                X++;
            } else if (board2[i][j] == MULTIPLY) {
                O++;
            } else {
                blank++;
            }
        }

        score+=changeInScore(X, O);

        blank = 0;
        X = 0;
        O = 0;

        //Check Diagonal (Second)
        for (int i = 2, j = 0; i > -1; --i, ++j) {
            if (board2[i][j] == CIRCLE) {
                X++;
            } else if (board2[i][j] == MULTIPLY) {
                O++;
            } else {
                blank++;
            }
        }

        score+=changeInScore(X, O);

        return score;
    }

    private int changeInScore(int X, int O){
        int change;
        if (X == 3) {
            change = 100;
        } else if (X == 2 && O == 0) {
            change = 10;
        } else if (X == 1 && O == 0) {
            change = 1;
        } else if (O == 3) {
            change = -100;
        } else if (O == 2 && X == 0) {
            change = -10;
        } else if (O == 1 && X == 0) {
            change = -1;
        } else {
            change = 0;
        }
        return change;
    }

    public boolean isGameOver() {
        //Game is over is someone has won, or board is full (draw)
        return (circleWon() || multiplyWon() || getAvailableStates().isEmpty());
    }


    public boolean circleWon() {
        if ((board2[0][0] == board2[1][1] && board2[0][0] == board2[2][2] && board2[0][0] == CIRCLE) || (board2[0][2] == board2[1][1] && board2[0][2] == board2[2][0] && board2[0][2] == CIRCLE)) {
            //System.out.println("X Diagonal Win");
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if (((board2[i][0] == board2[i][1] && board2[i][0] == board2[i][2] && board2[i][0] == CIRCLE)
                    || (board2[0][i] == board2[1][i] && board2[0][i] == board2[2][i] && board2[0][i] == CIRCLE))) {
                // System.out.println("X Row or Column win");
                return true;
            }
        }
        return false;
    }

    public boolean multiplyWon() {
        if ((board2[0][0] == board2[1][1] && board2[0][0] == board2[2][2] && board2[0][0] == MULTIPLY) || (board2[0][2] == board2[1][1] && board2[0][2] == board2[2][0] && board2[0][2] == MULTIPLY)) {
            // System.out.println("O Diagonal Win");
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if ((board2[i][0] == board2[i][1] && board2[i][0] == board2[i][2] && board2[i][0] == MULTIPLY)
                    || (board2[0][i] == board2[1][i] && board2[0][i] == board2[2][i] && board2[0][i] == MULTIPLY)) {
                //  System.out.println("O Row or Column win");
                return true;
            }
        }

        return false;
    }

    public List<States> getAvailableStates() {
        availables = new ArrayList<States>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board2[i][j] == 0) {
                    availables.add(new States(i, j));
                }
            }
        }
        return availables;
    }

    public void placeAMove(States states, int player) {
        board2[states.a][states.b] = player;   //player = 1 for X, 2 for O
    }

    public States returnBestMove() {
        int MAX = -100000;
        int best = -1;

        for (int i = 0; i < scoresOfStates.size(); ++i) {
            if (MAX < scoresOfStates.get(i).score) {
                MAX = scoresOfStates.get(i).score;
                best = i;
            }
        }

        return scoresOfStates.get(best).state;
    }

    public void resetBoard() {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                board2[i][j] = NOT_PLAYED;
            }
        }
    }
//    private int minimax( int depth,int alpha, int beta, boolean isMaxTurn) {
//
//        if(beta<=alpha){ System.out.println("Pruning at depth = "+depth);if(!isMaxTurn) return Integer.MAX_VALUE; else return Integer.MIN_VALUE; }
//
//        if(depth == startdepth || EndedDraw()) return g_Method();
//
//        availables = getAvailables();
//
//        if(availables.isEmpty()) return 0;
//
//        if(depth==0) scoresOfStates.clear();
//
//        int maxValue = Integer.MIN_VALUE, minValue = Integer.MAX_VALUE;
//
//        for(int i=0;i<availables.size(); ++i){
//           int place = availables.get(i);
//
//            int currentScore = 0;
//
//            if(!isMaxTurn){
//                board[place] = CIRCLE;
//                currentScore = minimax(depth + 1, alpha, beta, true);
//                maxValue = Math.max(maxValue, currentScore);
//
//                //Set alpha
//                alpha = Math.max(currentScore, alpha);
//
//                if(depth == 0)
//                    scoresOfStates.add(new ScoresOfStates(currentScore, place));
//            }else if(isMaxTurn){
//                board[place] = MULTIPLY;
//                currentScore = minimax(depth + 1, alpha, beta, false);
//                minValue = Math.min(minValue, currentScore);
//
//                //Set beta
//                beta = Math.min(currentScore, beta);
//            }
//            //reset board
//            board[place] = NOT_PLAYED;
//
//            //If a pruning has been done, don't evaluate the rest of the sibling states
//            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break;
//        }
//        return !isMaxTurn ? maxValue : minValue;
//
//    }

//    private int g_Method(){
//        int score = 0;
//
//        //Diagonal Checking
//        for(int[] subDiag: diagonalCheck){
//            int countMultiply = 0;
//            int countCircle = 0;
//            for(int i = 0 ; i <subDiag.length ; i++){
//                if(subDiag[i] == MULTIPLY){countMultiply++;}else if(subDiag[i] == CIRCLE){countCircle++;}
//            }
//            score = updateScore(countMultiply,countCircle) + score;
//        }
//
//        //check rows
//        for(int[] subRows : rows){
//            int countMultiply = 0;
//            int countCircle = 0;
//            for(int i = 0 ; i < subRows.length ; i ++){
//                if(board[subRows[i]] == MULTIPLY){countMultiply++;}else if(board[subRows[i]] == CIRCLE){countCircle++;}
//            }
//            score = updateScore(countMultiply,countCircle) + score;
//        }
//
//        //check columns
//
//        for(int[] subColumn : columns){
//            int countMultiply = 0;
//            int countCircle = 0;
//            for(int i = 0 ; i<subColumn.length ; i++){
//                if(board[subColumn[i]] == MULTIPLY){countMultiply++;}else if(board[subColumn[i]] == CIRCLE){countCircle++;}
//            }
//            score = updateScore(countMultiply,countCircle);
//        }
//        return score;
//    }

//    private int updateScore(int countMultiply, int countCircle) {
//        int g = 0;
//
//        if(countMultiply == 1 && countCircle == 0) g = 1;
//        else if(countMultiply == 2 && countCircle == 0) g = 2;
//        else if(countMultiply == 3 && countCircle == 0) g = 10;
//        else if(countMultiply == 0 && countCircle == 1) g = -1;
//        else if(countMultiply == 0 && countCircle == 2) g = -2;
//        else if(countMultiply == 0 && countCircle == 3) g = -10;
//        return g;
//    }
    public class ScoresOfStates{
        States state;
        int score;
        public ScoresOfStates(int score, States state){
            this.score = score;
            this.state = state;
        }


}
    public class States{
        int a , b;
        public States(int a , int b){
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "("+a+" - "+b+")";
        }
    }

}
