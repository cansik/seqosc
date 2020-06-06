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

            //GUILayout.BeginHorizontal();
            GUILayout.Label("File");
            if (GUILayout.Button(player.HasBuffer ? Path.GetFileName(player.bufferFile) : "Select"))
            {
                var path = EditorUtility.OpenFilePanel("Open Buffer", Application.dataPath, "osc");

                if (path != "")
                {
                    player.LoadBuffer(path);
                }
            }
            //GUILayout.EndHorizontal();

            if (!player.HasBuffer) return;

            //GUILayout.BeginHorizontal();
            GUILayout.Label("Samples");
            GUILayout.Label($"{player.Buffer.Samples.Count}");
            //GUILayout.EndHorizontal();

            if (GUILayout.Button(player.IsPlaying ? "Stop" : "Play"))
            {
                if (!player.IsPlaying)
                    player.Play();
                else
                    player.Stop();
            }
        }
    }
}