import QtQuick 2.15
import QtQuick.Window 2.15
import MapView 1.0
import QtQuick.Controls 2.15

Window {
    width: 640
    height: 480
    visible: true
    title: qsTr("Hello World")

    Rectangle {
        color: "red"
        anchors.fill: parent
    }

    Text {
        text: "HELLO WORLD!"
        anchors.centerIn: parent
    }

    MapView {
        id: mapView
        anchors {
            fill: parent
        }

//        anchors {
//            topMargin: 100
//            bottomMargin: 100
//            leftMargin: 100
//            rightMargin: 100
//            fill: parent
//        }
//        anchors.fill: parent
//        anchors.centerIn: parent
    }

    Button {
        text: "hide"
        anchors {
            horizontalCenter: parent.horizontalCenter
            top: parent.top
        }
        onClicked: {
//            mapView.visible = 0
            console.log(mapView.height + ", " + mapView.width)
        }
    }

//    Component.onCompleted: {
//        console.log(mapView)
//        console.log("mapView")
//    }
}
