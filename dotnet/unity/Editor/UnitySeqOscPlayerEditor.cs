using System;
using System.IO;
using IAS_DigitalHumans.Scripts.FaceStream.seqosc.unity;
using ICSharpCode.NRefactory.Ast;
using UnityEditor;
using UnityEngine;

namespace SeqOSC
{
    [CustomEditor(typeof(UnitySeqOscPlayer))]
    public class UnitySeqOscPlayerEditor : Editor
    {
        public override void OnInspectorGUI()
        {
            base.OnInspectorGUI();
            var player = target as UnitySeqOscPlayer;
            Debug.Assert(player != null, "SeqOsc Player is not initialised yet!");
            
            if(GUILayout.Button(player.HasBuffer ? Path.GetFileName(player.bufferFile) : "Select Buffer"))
            {
                var path = EditorUtility.OpenFilePanel("Open Buffer", Application.dataPath, "osc");

                if (path != "")
                {
                    try
                    {
                        var data = File.ReadAllBytes(path);
                        var buffer = new OSCBuffer();
                        buffer.Read(data);

                        player.Buffer = buffer;
                        player.bufferFile = path;
                    }
                    catch (Exception ex)
                    {
                        player.Buffer = null;
                        Debug.LogException(ex);
                    }
                }
            }
            
            if(GUILayout.Button(player.IsPlaying ? "Stop" : "Play"))
            {
                player.Play();
            }
        }
    }
}