
// Import java libraries
import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;


public class splash extends javax.swing.JFrame {

    private int i;          // iterator for the progressBar state
    private Timer timer;    // Timer Object
    private TimerTask task; // TimerTask Object
    
    // Progress Bar events at specific intervals
    int[] progressArray = {25,59,70,85,100};
    
    /**
     * Constructor
     */
    public splash(){
        
        initComponents();
        setBackground(new Color(0,0,0,0));
        
        this.timer = new Timer();
        this.i = 0;
        
        this.task = new TimerTask() {
            @Override
            public void run() {
                
                // If the progressBar has not reached 100%
                if(i < progressArray.length) {
                    progress.setValue(progressArray[i++]);
                }
                else {
                    progress.setVisible(false);     // Do not display the progressBar
                    logoLabel.setVisible(false);    // Do not display the logo
                    
                    ideoneInterface.runIdeone();    // Run the Ideone
                    
                    dispose();          // Dispose this JFrame
                    task.cancel();      // Cancel the TimerTask
                    timer.cancel();     // Cancel the Timer
                }// end if
            }// end run()
        };
        
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }// end splash() constructor

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoLabel = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/avishkar.png"))); // NOI18N
        logoLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        progress.setForeground(new java.awt.Color(0, 153, 153));
        progress.setToolTipText("");
        progress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 92, 0), 2, true));
        progress.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(logoLabel)
            .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(logoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(240, 265));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel logoLabel;
    private javax.swing.JProgressBar progress;
    // End of variables declaration//GEN-END:variables
}
