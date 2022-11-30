#include "mapview.h"
#include <QGuiApplication>
#include <QScreen>
#include <QJniObject>
#include <QJniEnvironment>
#include <QDebug>

static const char qtAndroidMapViewControllerClass[] = "org/qtproject/example/mapview/MapViewController";
//static const char qtAndroidMapViewControllerClass[] = "org/qtproject/example/mapview/MapActivity";
//static const char qtAndroidMapViewControllerClass[] = "org/qtproject/example/mapview/MapView";

typedef QMap<quintptr, QQuickItem *> MapViews;
Q_GLOBAL_STATIC(MapViews, g_mapViews)

MapView::MapView(QQuickItem *parent):QQuickItem (parent)
    , m_id(reinterpret_cast<quintptr>(this))
    , m_callbackId(0)
    , m_window(0)
{
    m_viewController = QJniObject(qtAndroidMapViewControllerClass,
                                  "(Landroid/app/Activity;J)V",
                                  QJniObject(QNativeInterface::QAndroidApplication::context()).object<jobject>(),
                                  m_id);
//    m_viewController = QJniObject(qtAndroidMapViewControllerClass,
//                                  "(Landroid/app/Activity;J)V",
//                                  QJniObject(QNativeInterface::QAndroidApplication::context()).object<jobject>());
//    m_viewController.callMethod<void>("initializeMap");
//    m_mapView = m_viewController.callObjectMethod("getView",
//                                                  "()Lorg/qtproject/example/mapview/MapViewWrapper;");

//    qDebug() << "what am i: " << m_mapView.toString();
//    m_mapView = m_viewController.callObjectMethod("getWindow",
//                                                  "()Landroid/view/Window;");
//    m_mapView.callObjectMethod<void>("trigger");
//    m_mapView.callMethod<void>("trigger");
//    m_window = QWindow::fromWinId(reinterpret_cast<WId>(m_mapView.object()));
//    g_mapViews->insert(m_id, this);

//    connect(qGuiApp,&QGuiApplication::applicationStateChanged,this,&MapView::appStateChanged);
//    SetNewAppState(APP_STATE_CREATE);
}

MapView::~MapView(){
    SetNewAppState(APP_STATE_DESTROY);

    g_mapViews->take(m_id);
    if (m_window != 0) {
        m_window->setVisible(false);
        m_window->setParent(0);
        delete m_window;
    }

    m_viewController.callMethod<void>("destroy");
}

void MapView::appStateChanged(Qt::ApplicationState State){
    SetNewAppState((State == Qt::ApplicationActive) ? APP_STATE_START : APP_STATE_STOP);
}

void MapView::SetNewAppState(APP_STATE NewState){
    if(m_mapView.isValid()){
       m_mapView.callMethod<void>("appStateChanged","(I)V",NewState);
    }
}

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
