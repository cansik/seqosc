using System.IO;
using System.Net;
using SeqOSC;
using UnityEngine;

namespace IAS_DigitalHumans.Scripts.FaceStream.seqosc.unity
{
    public class UnitySeqOscPlayer : MonoBehaviour
    {
        private readonly OSCPlayer _player = new OSCPlayer();
        
        public float speed = 1.0f;
        public string host = IPAddress.Loopback.ToString();
        public int port = 8000;
        
        public bool IsPlaying => _player.IsPlaying;
        public bool HasBuffer => _player.Buffer != null;

        public OSCBuffer Buffer
        {
            get => _player.Buffer;
            set => _player.Buffer = value;
        }

        [HideInInspector]
        public string bufferFile;

        public void Play()
        {
            if (IsPlaying)
            {
                Debug.LogWarning("SeqOSC: Already playing!");
                return;
            }
            
            if (!HasBuffer)
            {
                Debug.LogWarning("SeqOSC: No buffer loaded!");
                return;
            }
            
            // update fields
            _player.Host = host;
            _player.Port = port;
            _player.Speed = speed;

            _player.Play();
        }
    }
}