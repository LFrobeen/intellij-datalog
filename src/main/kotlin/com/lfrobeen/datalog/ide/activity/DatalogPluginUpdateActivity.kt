package com.lfrobeen.datalog.ide.activity

import com.intellij.notification.*
import com.intellij.notification.impl.NotificationsManagerImpl
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.util.io.StreamUtil
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.impl.IdeFrameImpl
import com.intellij.ui.BalloonLayoutData
import com.intellij.ui.BalloonLayoutImpl
import com.intellij.ui.awt.RelativePoint
import com.lfrobeen.datalog.ide.component.DatalogComponent
import com.lfrobeen.datalog.ide.icons.DatalogIcons


class DatalogPluginUpdateActivity : StartupActivity {
    private val notificationDisplayId = "Datalog Plugin"

    private val notificationHeading = "Greetings from Datalog Plugin!"
    private val notificationContent by lazy {
        val stream = javaClass.classLoader.getResourceAsStream("datalog/ide/notifications/update.html")
        StreamUtil.convertSeparators(StreamUtil.readText(stream, "UTF-8"))
    }

    private val notificationGroup = NotificationGroup(
        notificationDisplayId,
        NotificationDisplayType.STICKY_BALLOON,
        false, null,
        DatalogIcons.MAIN
    )

    override fun runActivity(project: Project) {
        if (!DatalogComponent.getInstance().updated) {
            return
        }

        val frame = WindowManager.getInstance().getIdeFrame(project)

        val notification = notificationGroup
            .createNotification(NotificationType.INFORMATION)
            .setIcon(DatalogIcons.MAIN)
            .setTitle(notificationHeading)
            .setContent(notificationContent)
            .setListener(NotificationListener.URL_OPENING_LISTENER)

        // TODO: review this
        if (frame != null) {
            val balloon =
                NotificationsManagerImpl.createBalloon(
                    frame,
                    notification,
                    true,
                    false,
                    BalloonLayoutData.fullContent()
                ) { }

            // frame.balloonLayout?.add(balloon, BalloonLayoutData.fullContent())
            balloon.show(RelativePoint.getNorthEastOf(frame.component), Balloon.Position.atRight)
        } else {
            notification.notify(project)
        }

    }
}

