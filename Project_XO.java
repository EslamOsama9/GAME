
// // coment : last edited 2:05 PM 12/31/2024
import javax.swing.*;
import java.awt.*;

public class Project_XO extends JFrame {

    private int currentPlayer = 1; // 1 for 'X', 2 for 'O'
    private int[][] grid;
    private JLabel statusLabel = new JLabel("Player X's turn");
    private JButton[][] buttons;
    private int n; // Number of rows and columns
    private boolean gameStarted = false; // Track if the game has started

    public Project_XO(int n, JFrame mainFrame) {
        this.n = n;
        grid = new int[n][n];
        buttons = new JButton[n][n];

        // Close the main frame
        mainFrame.dispose();

        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);

        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null); // centered
        setLayout(new BorderLayout());

        // Set up status label
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.DARK_GRAY);
        add(statusLabel, BorderLayout.NORTH);

        // Create and add grid of buttons
        JPanel gridPanel = new JPanel(new GridLayout(n, n));
        gridPanel.setBackground(new Color(240, 240, 240));
        initializeButtons(gridPanel);
        add(gridPanel, BorderLayout.CENTER);

        // Add reset and back buttons
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(200, 220, 240));

        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.setFocusPainted(false);
        resetButton.setBackground(new Color(220, 53, 69));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(e -> {
            if (!gameStarted) {
                JOptionPane.showMessageDialog(this, "You need to play first before resetting!", "Reset Error",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                resetGame();
            }
        });
        bottomPanel.add(resetButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setFocusPainted(false);
        backButton.setBackground(new Color(40, 167, 69));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            this.dispose();
            Project_XO.main(new String[] {});
        });
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Frame background color
        getContentPane().setBackground(new Color(250, 250, 250));
        setVisible(true);

    }

    private void initializeButtons(JPanel gridPanel) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buttons[i][j] = new JButton(""); // Initialize each button
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(new Color(236, 240, 241));

                final int row = i; // Make row effectively final/ const
                final int col = j; // same

                buttons[i][j].addActionListener(e -> {
                    gameStarted = true;
                    MakeMove(row, col, buttons[row][col]);
                });
                gridPanel.add(buttons[i][j]);
            }
        }
    }

    private void MakeMove(int i, int j, JButton button) {
        if (grid[i][j] != 0 || getResult() != -1) {
            return; // not empty or game ended
        }
        grid[i][j] = currentPlayer;
        button.setText(currentPlayer == 1 ? "X" : "O");
        button.setForeground(currentPlayer == 1 ? Color.RED : Color.BLUE);

        int result = getResult();

        if (result == 1 || result == 2) { // Winner
            JOptionPane.showMessageDialog(this,
                    "Player " + (result == 1 ? "X" : "O") + " wins!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            resetGame();
        } else if (result == 0) { // Draw
            JOptionPane.showMessageDialog(this,
                    "It's a draw!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            resetGame();
        } else { // Continue game
            currentPlayer ^= 3; // switching between 1 and 2
            statusLabel.setText("Player " + (currentPlayer == 1 ? "X" : "O") + "'s turn");
        }
    }

    private int getResult() {
        int target = n;

        for (int i = 0; i < n; i++) {
            int rowCount = 1;
            for (int j = 1; j < n; j++) {
                if (grid[i][j] != 0 && grid[i][j] == grid[i][j - 1]) {
                    rowCount++;
                    if (rowCount == target) {
                        return grid[i][j];
                    }
                } else {
                    rowCount = 1;
                }
            }
        }

        for (int j = 0; j < n; j++) {
            int colCount = 1;
            for (int i = 1; i < n; i++) {
                if (grid[i][j] != 0 && grid[i][j] == grid[i - 1][j]) {
                    colCount++;
                    if (colCount == target) {
                        return grid[i][j];
                    }
                } else {
                    colCount = 1;
                }
            }
        }

        for (int i = 0; i <= n - target; i++) {
            for (int j = 0; j <= n - target; j++) {
                int diagCount1 = 0, diagCount2 = 0;
                for (int k = 0; k < target; k++) {
                    if (grid[i + k][j + k] != 0 && grid[i + k][j + k] == grid[i][j]) {
                        diagCount1++;
                    }
                    if (grid[i + k][j + target - 1 - k] != 0
                            && grid[i + k][j + target - 1 - k] == grid[i][j + target - 1]) {
                        diagCount2++;
                    }
                    if (diagCount1 == target || diagCount2 == target) {
                        return grid[i][j];
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    return -1;
                }
            }
        }

        return 0; // It's a draw
    }

    private void resetGame() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = 0; // Reset game grid
                buttons[i][j].setText(""); // Clear button text
                buttons[i][j].setForeground(Color.BLACK);
            }
        }
        currentPlayer = 1; // Reset to Player X
        statusLabel.setText("Player X's turn");
        gameStarted = false;
    }

    public static void main(String[] args) {

        JFrame mainFrame = new JFrame("Main Form");
        mainFrame.setSize(500, 500);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setMinimumSize(new Dimension(500, 500));

        // Set layout to GridBagLayout
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.getContentPane().setBackground(new Color(30, 30, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margin around buttons

        // Create buttons with custom colors
        JButton lowFormButton = new JButton("Low Level");
        lowFormButton.setPreferredSize(new Dimension(150, 50));
        lowFormButton.setBackground(new Color(70, 130, 180));
        lowFormButton.setForeground(Color.WHITE);
        lowFormButton.setFocusPainted(false);
        lowFormButton.setFont(new Font("Arial", Font.BOLD, 14));
        lowFormButton.addActionListener(e -> new Project_XO(3, mainFrame));

        JButton mdFormButton = new JButton("Medium Level");
        mdFormButton.setPreferredSize(new Dimension(150, 50));
        mdFormButton.setBackground(new Color(34, 139, 34));
        mdFormButton.setForeground(Color.WHITE);
        mdFormButton.setFocusPainted(false);
        mdFormButton.setFont(new Font("Arial", Font.BOLD, 14));
        mdFormButton.addActionListener(e -> new Project_XO(4, mainFrame));

        JButton highFormButton = new JButton("High Level");
        highFormButton.setPreferredSize(new Dimension(150, 50));
        highFormButton.setBackground(new Color(178, 34, 34));
        highFormButton.setForeground(Color.WHITE);
        highFormButton.setFocusPainted(false);
        highFormButton.setFont(new Font("Arial", Font.BOLD, 14));
        highFormButton.addActionListener(e -> new Project_XO(5, mainFrame));

        JButton TipFormButton = new JButton("Tips");
        TipFormButton.setPreferredSize(new Dimension(150, 50));
        TipFormButton.setBackground(new Color(255, 215, 0));
        TipFormButton.setForeground(Color.BLACK);
        TipFormButton.setFocusPainted(false);
        TipFormButton.setFont(new Font("Arial", Font.BOLD, 14));
        TipFormButton.addActionListener(
                e -> JOptionPane.showMessageDialog(mainFrame,
                        "Welcome to the famous TicTacToe game!\nThe rules are very simple !\nplayers take turn placing 'X' for player1\n or 'O' for player2 in boxes\nfirst player to get 3 consective marks on the board\ndiagonal , row or column wins !\nplayer 1 starts the game\n"));

        // Add buttons to the frame
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainFrame.add(lowFormButton, gbc);

        gbc.gridy = 1;
        mainFrame.add(mdFormButton, gbc);

        gbc.gridy = 2;
        mainFrame.add(highFormButton, gbc);

        gbc.gridy = 3;
        mainFrame.add(TipFormButton, gbc);

    }
}