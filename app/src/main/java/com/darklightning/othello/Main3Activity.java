package com.darklightning.othello;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.Toast;

import static com.darklightning.othello.MySquares.blackturn;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "rikki";
    public static int blackcount=2,whitecount=2;
    int greatest;
    int length=0;
    MySquares squares[][] ;
    Button[] playbuttons;
    Button[] tiles;
    LinearLayout mainlayout;
    LinearLayout grid ,taskbar,playbar,tilebar;
    boolean turn,hinting;
    LinearLayout[] rows;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainlayout = (LinearLayout)findViewById(R.id.activity_main);
        setUpBoard();

    }
    private void setUpBoard()
    {


        rows = new LinearLayout[8];
        squares = new MySquares[8][8];

        setTaskbar();
        setGrid();
        setPlaybar();
        displayTiles();
        fourSquares();
        showcount();
        showHints();
        comturn();

    }


    void fourSquares()
    {
        squares[3][3].setBackgroundColor(Color.parseColor("white"));
        squares[3][3].isblack=false;
        squares[3][3].ischecked=true;
        squares[3][4].setBackgroundColor(Color.parseColor("black"));
        squares[3][4].isblack=true;
        squares[3][4].ischecked=true;
        squares[4][3].setBackgroundColor(Color.parseColor("black"));
        squares[4][3].isblack=true;
        squares[4][3].ischecked=true;
        squares[4][4].setBackgroundColor(Color.parseColor("white"));
        squares[4][4].isblack=false;
        squares[4][4].ischecked=true;
        whitecount=2;
        blackcount=2;
        showcount();
    }
    void setGrid()
    {
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        grid = new LinearLayout(this);
        LinearLayout.LayoutParams param1=new LinearLayout.LayoutParams(width,width);
        param1.setMargins(0,0,0,0);
        grid.setPadding(50,50,50,50);
        grid.setBackgroundResource(R.drawable.board);
        grid.setLayoutParams(param1);
        grid.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<8;i++)
        {
            rows[i] = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(width-100)/8);
            rows[i].setLayoutParams(params);
            rows[i].setOrientation(LinearLayout.HORIZONTAL);
            grid.addView(rows[i]);

        }
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                squares[i][j]=new MySquares(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((width-100)/8,(width-100)/8);
                params.setMargins(1,1,0,1);
                squares[i][j].setLayoutParams(params);
                squares[i][j].setOnClickListener(this);
                squares[i][j].setBackgroundColor(Color.parseColor("#1E8449"));
                squares[i][j].m=i;
                squares[i][j].n=j;

                rows[i].addView(squares[i][j]);
            }
        }
        mainlayout.addView(grid);
    }
    void setTaskbar()
    {
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height= metrics.heightPixels;
        taskbar = new LinearLayout(this);
        LinearLayout.LayoutParams param2=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
        taskbar.setLayoutParams(param2);
        taskbar.setOrientation(LinearLayout.HORIZONTAL);
        final Button[] taskbuttons;
        taskbuttons = new Button[4];
        for(int i=0;i<4;i++)
        {

            taskbuttons[i] = new Button(this);
            LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(0,height/11, 1);
            param3.setMargins(10,20,10,10);
            taskbuttons[i].setLayoutParams(param3);
            taskbar.addView(taskbuttons[i]);
        }
        taskbuttons[3].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Button t = (Button) v;
                {
                    for(int i=0;i<8;i++)
                    {
                        for(int j=0;j<8;j++)
                        {
                            squares[i][j].setBackgroundColor(Color.parseColor("#1E8449"));
                            squares[i][j].m=i;
                            squares[i][j].n=j;
                            squares[i][j].isblack=false;
                            squares[i][j].ischecked=false;
                            squares[i][j].blackturn=false;
                        }
                    }
                    turn=false;
                    fourSquares();
                    showHints();

                }
            }
        });
        taskbuttons[0].setBackgroundResource(R.drawable.menu);
        taskbuttons[1].setBackgroundResource(R.drawable.hints);
        taskbuttons[2].setBackgroundResource(R.drawable.undo);
        taskbuttons[3].setBackgroundResource(R.drawable.restart);
        taskbuttons[0].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Main3Activity.this,Main2Activity.class);
                startActivity(intent);

            }
        });
        taskbuttons[1].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(hinting)
                {
                    hinting=false;
                }
                else
                {
                    hinting=true;
                }
                refresh();
                showHints();
            }
        });

        mainlayout.addView(taskbar);
    }

    void setPlaybar()
    {
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        playbuttons = new Button[2];
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        playbar = new LinearLayout(this);
        LinearLayout.LayoutParams param4=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
        int w= width/16;
        playbar.setLayoutParams(param4);
        playbar.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<2;i++)
        {

            playbuttons[i] = new Button(this);
            LinearLayout.LayoutParams param5 = new LinearLayout.LayoutParams(0,height/10, 1);
            param5.setMargins(w/2,w,w,2*w);
            playbuttons[i].setLayoutParams(param5);
            playbar.addView(playbuttons[i]);
        }
        showturn();
        mainlayout.addView(playbar);
    }
    void displayTiles()
    {
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        tiles = new Button[2];
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        tilebar = new LinearLayout(this);
        LinearLayout.LayoutParams param6=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
        int w= width/16;
        tilebar.setLayoutParams(param6);
        tilebar.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0;i<2;i++)
        {

            tiles[i] = new Button(this);
            LinearLayout.LayoutParams param5 = new LinearLayout.LayoutParams(0,height/12, 1);
            param5.setMargins(10,30,50,0);
            tiles[i].setLayoutParams(param5);
            tilebar.addView(tiles[i]);
        }
        tiles[0].setBackgroundResource(R.drawable.white);
        tiles[0].setTextSize(35);
        tiles[1].setBackgroundResource(R.drawable.black);
        tiles[1].setTextColor(Color.parseColor("white"));
        tiles[1].setTextSize(35);
        mainlayout.addView(tilebar);
    }
    void showcount()
    {
        tiles[0].setText(""+whitecount);
        tiles[1].setText(""+blackcount);
    }
    void showturn()
    {
        if(blackturn==false)
        {
            playbuttons[0].setBackgroundResource(R.drawable.player11);
            playbuttons[1].setBackgroundResource(R.drawable.player22);
        }
        else
        {
            playbuttons[0].setBackgroundResource(R.drawable.player12);
            playbuttons[1].setBackgroundResource(R.drawable.player21);
        }
    }
    public void count()
    {
        if(blackturn)
        {
            blackcount++;
            whitecount--;
        }
        else
        {
            whitecount++;
            blackcount--;
        }
    }
    public boolean check(int k ,int l)
    {
        if(k<0||l<0||k>7||l>7)
            return false;
        return true;
    }
    void game(int m,int n)
    {
        boolean flag=true;

        boolean dia1=false,dia2=false,hor=false,ver=false;
        for(int k = m-1;k<=m+1;k++)
        {
            for (int l = n - 1; l <= n + 1; l++)
            {

                Log.d(TAG,"0 "+m+n);
                if((k!=m)||(l!=n))
                {
                    Log.d(TAG,"1 "+k+l);
                    if(check(k,l))
                    {
                        Log.i(TAG,"2 ");
                        if(squares[k][l].isblack!=squares[k][l].blackturn||squares[k][l].ischecked==true)
                        {
                            Log.d(TAG,"3");
                            if((k==m-1&&l==n-1)||(k==m+1&&l==n+1))
                            {
                                Log.d(TAG,"4");
                                dia1=true;
                            }
                            if((k==m+1&&l==n-1)||(k==m-1&&l==n+1))
                            {
                                dia2=true;
                            }
                            if((k==m&&l==n-1)||(k==m&&l==n+1))
                            {
                                hor=true;
                            }
                            if((k==m-1&&l==n)||(k==m+1&&l==n))
                            {
                                ver=true;
                            }

                        }
                    }
                }
            }
        }
        //horizontal
        if(hor)
        {
            for(int j=0;j<8;j++) {
                if(squares[m][j].isblack==squares[m][j].blackturn&&squares[m][j].ischecked==true) {
                    if(j<n) {
                        for(int a=j+1;a<n;a++) {
                            if(squares[m][a].isblack==squares[m][j].blackturn||squares[m][a].ischecked!=true) {
                                flag=false;
                                break;
                            }
                            else {
                                flag=true;
                            }
                        }
                    }
                    else if(j>n) {
                        for(int a=n+1;a<j;a++) {
                            if(squares[m][a].isblack==squares[m][j].blackturn||squares[m][a].ischecked!=true) {
                                flag=false;
                                break;
                            }
                            else {
                                flag=true;
                            }
                        }
                    }
                    if(flag==true) {
                        if(j<n) {
                            for(int a=j+1;a<n;a++) {
                                squares[m][n].setColour();
                                count();
                                squares[m][n].ischecked = true;
                                squares[m][a].changeIsBlack();
                                turn=true;
                            }
                        }
                        else if(j>n) {
                            for(int a=n+1;a<j;a++) {
                                squares[m][n].setColour();
                                count();
                                squares[m][n].ischecked = true;
                                squares[m][a].changeIsBlack();
                                turn=true;
                            }
                        }
                    }

                }
            }
        }
        //vertical
        if(ver)
        {
            for(int i=0;i<8;i++) {
                if(squares[i][n].isblack==squares[i][n].blackturn&&squares[i][n].ischecked==true) {
                    if(i<m) {
                        for(int a=i+1;a<m;a++) {
                            if(squares[a][n].isblack==squares[i][n].blackturn||squares[a][n].ischecked!=true) {
                                flag=false;
                                break;
                            }
                            else {
                                flag=true;
                            }
                        }
                    }
                    else if(i>m)
                    {
                        for(int a=m+1;a<i;a++) {
                            if(squares[a][n].isblack==squares[i][n].blackturn||squares[a][n].ischecked!=true) {
                                flag=false;
                                break;
                            }
                            else {
                                flag=true;
                            }
                        }
                    }
                    if(flag==true)
                    {
                        if(i<m) {
                            for(int a=i+1;a<m;a++)
                            {
                                squares[m][n].setColour();
                                count();
                                squares[m][n].ischecked = true;
                                squares[a][n].changeIsBlack();
                                turn=true;
                            }
                        }
                        else if(i>m)
                        {
                            for(int a=m+1;a<i;a++)
                            {
                                squares[m][n].setColour();
                                count();
                                squares[m][n].ischecked = true;
                                squares[a][n].changeIsBlack();
                                turn=true;
                            }
                        }
                    }

                }
            }
        }
        //diagonal1
        if(dia1)
        {
            int size = 8 - Math.abs(m - n);

            if (m >= n) {
                for (int i = m - n, j = 0; j < size; i++, j++) {
                    if (squares[i][j].isblack == squares[i][j].blackturn && squares[i][j].ischecked == true) {
                        if (j < n) {
                            for (int a = i + 1, b = j + 1; a < m; a++, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;
                                } else {
                                    flag = true;
                                }
                            }
                        } else if (j > n) {
                            for (int a = m + 1, b = n + 1; a < i; a++, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;

                                } else {
                                    flag = true;
                                }
                            }
                        }
                        if (flag == true) {

                            if (j < n) {
                                for (int a = i + 1, b = j + 1; a < m; a++, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            } else if (j > n) {
                                for (int a = m + 1, b = n + 1; a < i; a++, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            }
                        }

                    }
                }
            } else if (m < n) {
                for (int j = n - m, i = 0; i < size; i++, j++) {
                    if (squares[i][j].isblack == squares[i][j].blackturn && squares[i][j].ischecked == true) {
                        if (j < n) {
                            for (int a = i + 1, b = j + 1; a < m; a++, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;
                                } else {
                                    flag = true;
                                }
                            }
                        } else if (j > n) {
                            for (int a = m + 1, b = n + 1; a < i; a++, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;
                                } else {
                                    flag = true;
                                }
                            }
                        }
                        if (flag == true) {
                            if (j < n) {
                                for (int a = i + 1, b = j + 1; a < m; a++, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            } else if (j > n) {
                                for (int a = m + 1, b = n + 1; a < i; a++, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            }
                        }

                    }
                }
            }
        }
        //diagonal2

        if(dia2) {
            if (m + n < 8) {
                int size2 = m + n + 1;
                for (int i = 0, j = m + n; i < size2; i++, j--) {
                    if (squares[i][j].isblack == squares[i][j].blackturn && squares[i][j].ischecked == true) {
                        if (j < n) {
                            for (int a = i - 1, b = j + 1; a > m; a--, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;
                                } else {
                                    flag = true;
                                }
                            }
                        } else if (j > n) {
                            for (int a = m - 1, b = n + 1; b < j; a--, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;

                                } else {
                                    flag = true;
                                }
                            }
                        }
                        if (flag == true) {
                            if (j < n) {
                                for (int a = i - 1, b = j + 1; a > m; a--, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            } else if (j > n) {
                                for (int a = m - 1, b = n + 1; b < j; a--, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            }
                        }

                    }
                }
            }

            if (m + n >= 8) {
                int size2 = 15 - m - n;
                for (int i = m + n - 7, j = 7, x = 0; x < size2; i++, j--, x++) {
                    if (squares[i][j].isblack == squares[i][j].blackturn && squares[i][j].ischecked == true) {
                        if (j < n) {
                            for (int a = i - 1, b = j + 1; a > m; a--, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;
                                } else {
                                    flag = true;
                                }
                            }
                        } else if (j > n) {
                            for (int a = m - 1, b = n + 1; b < j; a--, b++) {
                                if (squares[a][b].isblack == squares[a][b].blackturn || squares[a][b].ischecked != true) {
                                    flag = false;
                                    break;
                                } else {
                                    flag = true;
                                }
                            }
                        }
                        if (flag == true) {
                            if (j < n) {
                                for (int a = i - 1, b = j + 1; a > m; a--, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            } else {
                                for (int a = m - 1, b = n + 1; b < j; a--, b++) {
                                    count();
                                    setCickfun(a, b, m, n);
                                }
                            }
                        }

                    }
                }
            }
        }
        if(turn)
        {
            if(blackturn)
            {
                blackcount++;
            }
            else
            {
                whitecount++;
            }
            showcount();
            squares[m][n].changeBlackTurn();
            refresh();
            showturn();
            showHints();
            turn=false;

        }

    }


    void showHints()
    {
        if(hinting==false)
        {
            boolean flag=false;

            for (int o=0;o<8;o++)

            {
                for(int p=0;p<8;p++)
                {

                    if(o+p<8)
                    {
                        int size2 = o+p+1;
                        for(int i=0,j=o+p;i<size2;i++,j--)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i-1,b=j+1 ; a>o; a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }

                                else if(j>p)
                                {
                                    for(int a=o-1,b=p+1;b<j;a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;

                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {
                                    if(j<p)
                                    {
                                        for(int a=i-1,b=j+1;a>o;a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    else if(j>p)
                                    {
                                        for(int a=o-1,b=p+1;b<j;a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    setGreatest();
                                }

                            }
                        }
                    }
                    if(o+p>=8)
                    {
                        int size2 = 15-o-p;
                        for(int i=o+p-7,j=7,x=0;x<size2;i++,j--,x++)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i-1,b=j+1 ; a>o; a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                else if(j>p)
                                {
                                    for(int a=o-1,b=p+1;b<j;a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {
                                    if(j<p)
                                    {
                                        for(int a=i-1,b=j+1 ; a>o; a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        for(int a=o-1,b=p+1;b<j;a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    setGreatest();
                                }

                            }
                        }
                    }
                    int size = 8-Math.abs(o-p);
                    if(o>=p)
                    {
                        for(int i=o-p,j=0;j<size;i++,j++)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i+1,b=j+1;a<o;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                else if(j>p)
                                {
                                    for(int a=o+1,b=p+1;a<i;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;

                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {

                                    if(j<p)
                                    {
                                        for(int a=i+1,b=j+1;a<o;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    else if(j>p)
                                    {
                                        for(int a=o+1,b=p+1;a<i;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    setGreatest();
                                }

                            }
                        }
                    }
                    else if(o<p)
                    {
                        for(int j=p-o,i=0;i<size;i++,j++)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i+1,b=j+1;a<o;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                else if(j>p)
                                {
                                    for(int a=o+1,b=p+1;a<i;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {
                                    if(j<p)
                                    {
                                        for(int a=i+1,b=j+1;a<o;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    else if(j>p)
                                    {
                                        for(int a=o+1,b=p+1;a<i;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                                length++;
                                            }
                                        }
                                    }
                                    setGreatest();
                                }

                            }
                        }
                    }
                    //vertical

                    for(int i=0;i<8;i++)
                    {
                        if(squares[i][p].isblack==squares[i][p].blackturn&&squares[i][p].ischecked==true)
                        {
                            if(i<o)
                            {
                                for(int a=i+1;a<o;a++)
                                {
                                    if(squares[a][p].isblack==squares[i][p].blackturn||squares[a][p].ischecked!=true)
                                    {
                                        flag=false;
                                        break;
                                    }
                                    else
                                    {
                                        flag=true;
                                        length++;

                                    }
                                }
                            }
                            else if(i>o)
                            {
                                for(int a=o+1;a<i;a++)
                                {
                                    if(squares[a][p].isblack==squares[i][p].blackturn||squares[a][p].ischecked!=true)
                                    {
                                        flag=false;
                                        break;
                                    }
                                    else
                                    {
                                        flag=true;
                                        length++;
                                    }
                                }
                            }
                            if(length>greatest)
                            {
                                greatest=length;
                            }
                            if(flag==true)
                            {
                                if(i<o) {
                                    for(int a=i+1;a<o;a++)
                                    {
                                        if(squares[o][p].ischecked==false)
                                        {
                                            squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            length++;
                                        }
                                    }
                                }
                                else if(i>o)
                                {
                                    for(int a=o+1;a<i;a++)
                                    {
                                        if(squares[o][p].ischecked==false)
                                        {
                                            squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            length++;
                                        }
                                    }
                                }
                                setGreatest();
                            }

                        }
                    }
                    //horizontal
                    for(int j=0;j<8;j++)
                    {
                        if(squares[o][j].isblack==squares[o][j].blackturn&&squares[o][j].ischecked==true) {
                            if(j<p) {
                                for(int a=j+1;a<p;a++) {
                                    if(squares[o][a].isblack==squares[o][j].blackturn||squares[o][a].ischecked!=true) {
                                        flag=false;
                                        break;
                                    }
                                    else {
                                        flag=true;
                                    }
                                }
                            }
                            else if(j>p) {
                                for(int a=p+1;a<j;a++) {
                                    if(squares[o][a].isblack==squares[o][j].blackturn||squares[o][a].ischecked!=true) {
                                        flag=false;
                                        break;
                                    }
                                    else {
                                        flag=true;
                                    }
                                }
                            }
                            if(flag==true)
                            {
                                if(j<p)
                                {
                                    for(int a=j+1;a<p;a++)
                                    {
                                        length++;
                                        if(squares[o][p].ischecked==false)
                                        {
                                            squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));//change p to a

                                        }
                                    }
                                }
                                else if(j>p)
                                {
                                    for(int a=p+1;a<j;a++)
                                    {
                                        length++;
                                        if(squares[o][p].ischecked==false)
                                        {
                                            squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));//change p to a

                                        }
                                    }
                                }
                                setGreatest();
                            }

                        }
                    }
                }

            }

        }
    }
   void setGreatest()
   {
       if(length>greatest)
       {
           greatest=length;
       }
       length=0;
   }
    void comturn()
    {
        if(hinting==false)
        {
            boolean flag=false;

            for (int o=0;o<8;o++)

            {
                for(int p=0;p<8;p++)
                {

                    if(o+p<8)
                    {
                        int size2 = o+p+1;
                        for(int i=0,j=o+p;i<size2;i++,j--)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i-1,b=j+1 ; a>o; a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }

                                else if(j>p)
                                {
                                    for(int a=o-1,b=p+1;b<j;a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;

                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {
                                    if(j<p)
                                    {
                                        for(int a=i-1,b=j+1;a>o;a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                    else if(j>p)
                                    {
                                        for(int a=o-1,b=p+1;b<j;a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    if(o+p>=8)
                    {
                        int size2 = 15-o-p;
                        for(int i=o+p-7,j=7,x=0;x<size2;i++,j--,x++)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i-1,b=j+1 ; a>o; a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                else if(j>p)
                                {
                                    for(int a=o-1,b=p+1;b<j;a--,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {
                                    if(j<p)
                                    {
                                        for(int a=i-1,b=j+1 ; a>o; a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                    else
                                    {
                                        for(int a=o-1,b=p+1;b<j;a--,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    int size = 8-Math.abs(o-p);
                    if(o>=p)
                    {
                        for(int i=o-p,j=0;j<size;i++,j++)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i+1,b=j+1;a<o;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                else if(j>p)
                                {
                                    for(int a=o+1,b=p+1;a<i;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;

                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {

                                    if(j<p)
                                    {
                                        for(int a=i+1,b=j+1;a<o;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                    else if(j>p)
                                    {
                                        for(int a=o+1,b=p+1;a<i;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    else if(o<p)
                    {
                        for(int j=p-o,i=0;i<size;i++,j++)
                        {
                            if(squares[i][j].isblack==squares[i][j].blackturn&&squares[i][j].ischecked==true)
                            {
                                if(j<p)
                                {
                                    for(int a=i+1,b=j+1;a<o;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                else if(j>p)
                                {
                                    for(int a=o+1,b=p+1;a<i;a++,b++)
                                    {
                                        if(squares[a][b].isblack==squares[a][b].blackturn||squares[a][b].ischecked!=true)
                                        {
                                            flag=false;
                                            break;
                                        }
                                        else
                                        {
                                            flag=true;
                                        }
                                    }
                                }
                                if(flag==true)
                                {
                                    if(j<p)
                                    {
                                        for(int a=i+1,b=j+1;a<o;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                    else if(j>p)
                                    {
                                        for(int a=o+1,b=p+1;a<i;a++,b++)
                                        {
                                            if(squares[o][p].ischecked==false)
                                            {
                                                squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    //vertical
                    int greatest =0;
                    int length=0;
                    for(int i=0;i<8;i++)
                    {
                        if(squares[i][p].isblack==squares[i][p].blackturn&&squares[i][p].ischecked==true)
                        {
                            if(i<o)
                            {
                                for(int a=i+1;a<o;a++)
                                {
                                    if(squares[a][p].isblack==squares[i][p].blackturn||squares[a][p].ischecked!=true)
                                    {
                                        flag=false;
                                        break;
                                    }
                                    else
                                    {
                                        flag=true;
                                        length++;

                                    }
                                }
                            }
                            else if(i>o)
                            {
                                for(int a=o+1;a<i;a++)
                                {
                                    if(squares[a][p].isblack==squares[i][p].blackturn||squares[a][p].ischecked!=true)
                                    {
                                        flag=false;
                                        break;
                                    }
                                    else
                                    {
                                        flag=true;
                                        length++;
                                    }
                                }
                            }
                            if(length>greatest)
                            {
                                greatest=length;
                            }
                            if(flag==true)
                            {
                                if(i<o) {
                                    for(int a=i+1;a<o;a++)
                                    {
                                        if(squares[o][p].ischecked==false)
                                        {
                                            squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                        }
                                    }
                                }
                                else if(i>o)
                                {
                                    for(int a=o+1;a<i;a++)
                                    {
                                        if(squares[o][p].ischecked==false)
                                        {
                                            squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                        }
                                    }
                                }
                            }

                        }
                    }
                    //horizontal
                    for(int j=0;j<8;j++) {
                        if(squares[o][j].isblack==squares[o][j].blackturn&&squares[o][j].ischecked==true) {
                            if(j<p) {
                                for(int a=j+1;a<p;a++) {
                                    if(squares[o][a].isblack==squares[o][j].blackturn||squares[o][a].ischecked!=true) {
                                        flag=false;
                                        length=0;
                                        break;
                                    }
                                    else {
                                        length++;
                                        flag=true;
                                    }
                                }
                            }
                            else if(j>p) {
                                for(int a=p+1;a<j;a++) {
                                    if(squares[o][a].isblack==squares[o][j].blackturn||squares[o][a].ischecked!=true) {
                                        flag=false;
                                        length=0;
                                        break;
                                    }
                                    else {
                                        length++;
                                        flag=true;
                                    }
                                }
                            }
                            if(flag==true&&length==greatest) {
                                if(j<p) {
                                    for(int a=j+1;a<p;a++) {
                                        if(squares[o][a].ischecked==false)
                                        {
                                            squares[o][p].setBackgroundColor(Color.parseColor("#0CA94E"));
                                        }
                                    }
                                }
                                else if(j>p) {
                                    for(int a=p+1;a<j;a++) {
                                        if(squares[o][p].ischecked==false)
                                        {
                                            squares[o][a].setBackgroundColor(Color.parseColor("#0CA94E"));
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

            }

        }
    }

    void refresh()
    {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(squares[i][j].ischecked==false)
                {
                    squares[i][j].setBackgroundColor(Color.parseColor("#1E8449"));
                }
            }
        }
    }
    void setCickfun(int a,int b,int m,int n)
    {
        squares[m][n].setColour();
        squares[m][n].ischecked = true;
        squares[a][b].changeIsBlack();
        turn=true;

    }
    @Override
    public void onClick(View v)
    {
        MySquares s = (MySquares)v;
        if(s.ischecked==false)
        {

            game(s.m, s.n);
        }
    }
}

