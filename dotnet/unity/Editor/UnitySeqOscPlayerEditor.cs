using IAS_DigitalHumans.Scripts.FaceStream.seqosc.unity;
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
            
            if(GUILayout.Button("Select"))
            {
                var objectField = (Texture2D)EditorGUILayout.ObjectField("My Texture", null, typeof(Texture2D), false);
            }
            
            if(GUILayout.Button(player.IsPlaying ? "Stop" : "Play"))
            {
                player.Play();
            }
        }
    }
}