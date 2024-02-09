//import java.util.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import algorithms.BubbleSort;
import algorithms.SortAlgorithm;

public class App{

    private final JFrame frame;
    
    public static final int MIN_WIDTH = 1600;
    public static final int MIN_HEIGHT = 900;

    public App() {
        //window = new JPanel();
        frame = new JFrame("Sorting Algorithm Visualiser");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[] data = new int[]{1,6,4,6,8,3,4,25,7,3,6,8,4,6,3,4,2};

        frame.setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        DataVisualiserPanel arrayPanel = new DataVisualiserPanel(frame);
        
        frame.setLayout(null);
        frame.add(arrayPanel);
        frame.setContentPane(arrayPanel);
        arrayPanel.drawData(data);
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                arrayPanel.drawData(data);
            }
        });

        JButton sortButton = new JButton("Sort");
        sortButton.addMouseListener(new MouseListener(){
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                DataVisualiserPanel.sortData(data, "BubbleSort");
            }

        });
        frame.add(sortButton);

    }



    public void start() {
        frame.pack();
    }

    public static void main(String[] args) throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run(){
                new App().start();
            }
        });
    }

    public class DataVisualiserPanel extends JPanel{
        JFrame parent;

        private int gap = 8; // the gap between each "pillar" of data. (may need to be calculated for large amounts of data)
        private int dataWidth; // width of each "pillar" of data.
        private int currX, currY; // the current x and y position of the data "pillars"
        private float scaleRatio; // the ratio to scale the height of the "pillars" to
        private int[] data; // the array of data to be drawn on the JPanel
        private static SortAlgorithm currentAlgorithm; // the algorithm used to sort the data


        public DataVisualiserPanel(JFrame parent){
            this.parent = parent;
            this.setBounds(parent.getInsets().left, parent.getInsets().top, 
                 parent.getWidth() - 2*parent.getInsets().left, (parent.getHeight() - parent.getInsets().top) - parent.getInsets().bottom);
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.setColor(Color.BLACK);
            currX = gap;
            currY = getHeight() - gap;
            for (int datum : data){
                float height = scaleRatio * -datum;
                g.fillRect(currX, currY, dataWidth, (int)height);
                currX += (dataWidth + gap);
            }
        }

        public static void sortData(int[] data, String sortAlgorithm){
            currentAlgorithm = new BubbleSort(); // TODO find a way to programmatically create an object instead of hard-coding the BubbleSort() constructor.
        }

        private void drawData(int[] data) {
            this.data = data;
            // Calculating the width of each pillar of data using ((TOTAL_WIDTH - ((length+1) * gap)) / length)
            dataWidth = ((getWidth() - (data.length * gap)) / data.length);

            // Finding the max value in the data (assumes all data is positive)
            int max = 0;
            for (int datum : data){
                if (datum > max) max = datum;
            }

            scaleRatio = (float) (getHeight() - 2*gap) / max;
            
            repaint();
        }
    }
}