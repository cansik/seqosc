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
            DrawDefaultInspector();

            var incrementalUpdater = target as UnitySeqOscPlayer;
            if(GUILayout.Button("Play"))
            {
                
            }
            base.OnInspectorGUI();
        }
    }
}