#include "mapview.h"
#include <QGuiApplication>
#include <QScreen>
#include <QJniObject>
#include <QJniEnvironment>
#include <QDebug>

MapView::MapView(QQuickItem *parent):QQuickItem (parent)
    , m_id(reinterpret_cast<quintptr>(this))
    , m_callbackId(0)
    , m_window(0)
{
    m_viewController = QNativeInterface::QAndroidApplication::context();
}

MapView::~MapView(){
    //m_viewController.callMethod<void>("initializeMap");
//    SetNewAppState(APP_STATE_DESTROY);

//    g_mapViews->take(m_id);
//    if (m_window != 0) {
//        m_window->setVisible(false);
//        m_window->setParent(0);
//        delete m_window;
//    }

//    m_viewController.callMethod<void>("destroy");
}

//void MapView::appStateChanged(Qt::ApplicationState State){
//    SetNewAppState((State == Qt::ApplicationActive) ? APP_STATE_START : APP_STATE_STOP);
//}

//void MapView::SetNewAppState(APP_STATE NewState){
//    if(m_mapView.isValid()){
//       m_mapView.callMethod<void>("appStateChanged","(I)V",NewState);
//    }
//}

/*void activeFocusChanged(bool)
void activeFocusOnTabChanged(bool)
void antialiasingChanged(bool)
void baselineOffsetChanged(qreal)
void childrenRectChanged(const QRectF &)
void clipChanged(bool)
void containmentMaskChanged()
void enabledChanged()
void focusChanged(bool)
void heightChanged()
void implicitHeightChanged()
void implicitWidthChanged()
void opacityChanged()
void parentChanged(QQuickItem *)
void rotationChanged()
void scaleChanged()
void smoothChanged(bool)
void stateChanged(const QString &)
void transformOriginChanged(QQuickItem::TransformOrigin)
void visibleChanged()
void widthChanged()
void windowChanged(QQuickWindow *window)
void xChanged()
void yChanged()
void zChanged()*/
