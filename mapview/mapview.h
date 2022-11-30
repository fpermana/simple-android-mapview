#ifndef REMOTEVIEW_H
#define REMOTEVIEW_H
#pragma once

#include <QObject>
#include <QQuickItem>
#include <QQmlEngine>
#include <QJniObject>
#include <QWindow>
//#include <QtAndroid>
//#include <QtAndroidExtras>
//#include <QtAndroidExtras/QAndroidJniObject>
//#include<QtAndroidExtras/QAndroidJniEnvironment>

class MapView : public QQuickItem
{
    Q_OBJECT

public:
    MapView(QQuickItem*parent=nullptr);
    ~MapView();

    void setGeometry(const QRect &geometry);
    void setVisibility(QWindow::Visibility visibility);
    void setVisible(bool visible);

private slots:
    void appStateChanged(Qt::ApplicationState State);

//    void _activeFocusChanged(bool activeFocus);
//    void _activeFocusOnTabChanged(bool activeFocusOnTab);
//    void _antialiasingChanged(bool antialiasing);
//    void _baselineOffsetChanged(qreal baselineOffset);
//    void _childrenRectChanged(const QRectF &childrenRect);
//    void _clipChanged(bool clip);
//    void _containmentMaskChanged();
//    void _enabledChanged();
//    void _focusChanged(bool focus);
//    void _heightChanged();
//    void _implicitHeightChanged();
//    void _implicitWidthChanged();
//    void _opacityChanged();
//    void _parentChanged(QQuickItem *parent);
//    void _rotationChanged();
//    void _scaleChanged();
//    void _smoothChanged(bool smooth);
//    void _stateChanged(const QString &state);
//    void _transformOriginChanged(QQuickItem::TransformOrigin transformOrigin);
//    void _visibleChanged();
//    void _widthChanged();
//    void _windowChanged(QQuickWindow *window);
//    void _xChanged();
//    void _yChanged();
//    void _zChanged();
private :
#ifdef Q_OS_ANDROID

    enum APP_STATE
    {
        APP_STATE_CREATE = 0,
        APP_STATE_START,
        APP_STATE_STOP,
        APP_STATE_DESTROY
    };
    void SetNewAppState(APP_STATE NewState);

    quintptr m_id;
    quint64 m_callbackId;
    QWindow *m_window;
    QJniObject m_viewController;
    QJniObject m_mapView;
#endif
};

#endif // REMOTEVIEW_H
