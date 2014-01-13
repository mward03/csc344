using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using NAudio.Wave;
using System.Threading;
using System.Timers;

namespace AsgnOne
{
    public partial class Form1 : Form
    {
        private  NAudio.Wave.WaveFileReader wave = null;
        private NAudio.Wave.DirectSoundOut output = null;

        public Form1()
        {
            InitializeComponent();
        }

        private void open_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();

            if (ofd.ShowDialog() != DialogResult.OK) return;

            disposeWave();

            wave = new NAudio.Wave.WaveFileReader(ofd.FileName);
            output = new NAudio.Wave.DirectSoundOut();
            output.Init(new NAudio.Wave.WaveChannel32(wave));

            output.Play();

            playPause.Enabled = true;
            start.Enabled = true;
        }

        private void disposeWave()
        {
            if (output != null)
            {
                if (output.PlaybackState == NAudio.Wave.PlaybackState.Playing) output.Stop();
                output.Dispose();
                output = null;

            }

            if (wave != null)
            {
                wave.Dispose();
                wave = null;
            }
        }

        private void asgnOne_FormClosing(object sender, FormClosingEventArgs e)
        {
            disposeWave();
        }

        private void playPause_Click(object sender, EventArgs e)
        {
            if (output != null)
            {
                if (output.PlaybackState == NAudio.Wave.PlaybackState.Playing) output.Pause();
                else if (output.PlaybackState == NAudio.Wave.PlaybackState.Paused) output.Play();
            }
        }

        private void start_Click(object sender, EventArgs e)
        {
            
         
           
            System.Timers.Timer testTimer = new System.Timers.Timer(1000);
            testTimer.Elapsed += new ElapsedEventHandler(OnTimerElapsed);
            testTimer.Enabled = true;

        }


        public void OnTimerElapsed(object source, ElapsedEventArgs e)
        {
            Random random = new Random();
            int randomNumber = random.Next(0, 100);
            TimeSpan span = new TimeSpan(0, 0, 0, randomNumber, 0);
            WaveStreamExtensions.SetPosition(wave, span); 
        }
    }
}

public static class WaveStreamExtensions
{
    // Set position of WaveStream to nearest block to supplied position
    public static void SetPosition(this WaveStream strm, long position)
    {
        // distance from block boundary (may be 0)
        long adj = position % strm.WaveFormat.BlockAlign;
        // adjust position to boundary and clamp to valid range
        long newPos = Math.Max(0, Math.Min(strm.Length, position - adj));
        // set playback position
        strm.Position = newPos;
    }

    // Set playback position of WaveStream by seconds
    public static void SetPosition(this WaveStream strm, double seconds)
    {
        strm.SetPosition((long)(seconds * strm.WaveFormat.AverageBytesPerSecond));
    }

    // Set playback position of WaveStream by time (as a TimeSpan)
    public static void SetPosition(this WaveStream strm, TimeSpan time)
    {
        strm.SetPosition(time.TotalSeconds);
    }

    // Set playback position of WaveStream relative to current position
    public static void Seek(this WaveStream strm, double offset)
    {
        strm.SetPosition(strm.Position + (long)(offset * strm.WaveFormat.AverageBytesPerSecond));
    }
}