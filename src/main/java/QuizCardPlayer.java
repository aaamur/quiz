import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by sasha on 19.03.17.
 */
public class QuizCardPlayer {
    private JTextArea display;
    private JTextArea answer;
    private ArrayList<QuizCard> cardList;
    private JFrame frame;
    private QuizCard currentCard;
    private int currentCardIndex;
    private boolean isShowAnswer;
    private JButton nextButton;

    public static void main(String[] args) {
        QuizCardPlayer player = new QuizCardPlayer();
        player.go();
    }

    public void go() {

        frame = new JFrame("Quiz Card Player");
        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        display = new JTextArea(6, 20);
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        display.setFont(bigFont);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        nextButton = new JButton("Show answer");
        nextButton.addActionListener(new NextCardListener());

        mainPanel.add(qScroller);
        mainPanel.add(nextButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");

        loadMenuItem.addActionListener(new OpenMenuListener());

        fileMenu.add(loadMenuItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    private class NextCardListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (isShowAnswer) {
                display.setText(currentCard.getAnswer());
                nextButton.setText("Next card");
                isShowAnswer = false;
            } else {
                if (currentCardIndex < cardList.size()) {
                    showNextCard();
                } else {
                    display.setText("That was last card");
                    nextButton.setEnabled(false);
                }
            }
        }
    }

    private void showNextCard() {
        if (currentCardIndex < cardList.size()){
            currentCard = cardList.get(currentCardIndex);
            currentCardIndex++;
            display.setText(currentCard.getQuestion());
            nextButton.setText("Show answer");
            isShowAnswer = true;
        }

    }

    private class OpenMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileOpen = new JFileChooser();
            fileOpen.showOpenDialog(frame);
            loadFile(fileOpen.getSelectedFile());
        }

    }

    private void loadFile(File file) {
        cardList = new ArrayList<QuizCard>();
        currentCardIndex = 0;
        nextButton.setEnabled(true);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null) {
                makeCard(line);
            }
            reader.close();

        } catch (Exception ex) {
            System.out.println("Couldn't read the card file");
            ex.printStackTrace();
        }
        showNextCard();
    }

    private void makeCard(String line) {
        String[] result = line.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("made card");
    }
}

