namespace AsgnOne
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.open = new System.Windows.Forms.Button();
            this.playPause = new System.Windows.Forms.Button();
            this.gap = new System.Windows.Forms.Label();
            this.start = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // open
            // 
            this.open.Location = new System.Drawing.Point(12, 12);
            this.open.Name = "open";
            this.open.Size = new System.Drawing.Size(397, 54);
            this.open.TabIndex = 0;
            this.open.Text = "Open Wave";
            this.open.UseVisualStyleBackColor = true;
            this.open.Click += new System.EventHandler(this.open_Click);
            // 
            // playPause
            // 
            this.playPause.Enabled = false;
            this.playPause.Location = new System.Drawing.Point(12, 72);
            this.playPause.Name = "playPause";
            this.playPause.Size = new System.Drawing.Size(397, 52);
            this.playPause.TabIndex = 1;
            this.playPause.Text = "Play/Pause";
            this.playPause.UseVisualStyleBackColor = true;
            this.playPause.Click += new System.EventHandler(this.playPause_Click);
            // 
            // gap
            // 
            this.gap.AutoSize = true;
            this.gap.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.gap.Location = new System.Drawing.Point(196, 226);
            this.gap.Name = "gap";
            this.gap.Size = new System.Drawing.Size(26, 28);
            this.gap.TabIndex = 2;
            this.gap.Text = "0";
            // 
            // start
            // 
            this.start.Location = new System.Drawing.Point(12, 171);
            this.start.Name = "start";
            this.start.Size = new System.Drawing.Size(396, 52);
            this.start.TabIndex = 3;
            this.start.Text = "Start";
            this.start.UseVisualStyleBackColor = true;
            this.start.Click += new System.EventHandler(this.start_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(12F, 25F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(420, 281);
            this.Controls.Add(this.start);
            this.Controls.Add(this.gap);
            this.Controls.Add(this.playPause);
            this.Controls.Add(this.open);
            this.Name = "Form1";
            this.Text = "AsgnOne";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button open;
        private System.Windows.Forms.Button playPause;
        private System.Windows.Forms.Label gap;
        private System.Windows.Forms.Button start;
    }
}

