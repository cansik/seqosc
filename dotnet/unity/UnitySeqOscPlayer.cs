using System;
using System.IO;
using System.Net;
using System.Threading.Tasks;
using UnityEditor;
using UnityEngine;

namespace SeqOSC.Unity
{
    public class UnitySeqOscPlayer : MonoBehaviour
    {
        private readonly OSCPlayer _player = new OSCPlayer();

        public bool playOnStart = false;
        public float speed = 1.0f;
        public string host = IPAddress.Loopback.ToString();
        public int port = 8000;
        public bool loop = false;

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
            _player.Loop = loop;

            Task.Run(async () =>
            {
                await _player.Play();
                EditorUtility.SetDirty(this);
            });
        }

        public void Stop()
        {
            if (!IsPlaying)
                return;
            
            _player.Stop();
        }

        public void Start()
        {
            if (playOnStart && HasBuffer)
            {
                Play();
            }
        }

        public void OnDestroy()
        {
            if (IsPlaying)
            {
                Stop();
            }
        }

        public void OnValidate()
        {
            // reload buffer after start
            if (HasBuffer)
                return;

            if (File.Exists(bufferFile))
            {
                LoadBuffer(bufferFile);
            }
        }

        public void LoadBuffer(string path)
        {
            try
            {
                var data = File.ReadAllBytes(path);
                var buffer = new OSCBuffer();
                buffer.Read(data);

                Buffer = buffer;
                
                // store path (try to use relative if possible)
                bufferFile = path.Replace(Environment.CurrentDirectory + Path.DirectorySeparatorChar, "");
            }
            catch (Exception ex)
            {
                Buffer = null;
                Debug.LogException(ex);
            }
        }
    }
}