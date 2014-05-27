(ns sanic.core)

(enable-console-print!)

(def PIXI js/PIXI)

(def stage (PIXI/Stage. 0x888888))

(def renderer (.autoDetectRenderer PIXI 512 256))

(def game-container (PIXI/DisplayObjectContainer.))

(def loader (PIXI/AssetLoader. (clj->js
                                ["img/sanic.png"
                                 "img/bg-far.png"
                                 "img/bg-mid.png"])))

(def app (.getElementById js/document "app"))

(defn apply-gravity [sprite]
  (let [sprite-y (.-position.y sprite)]
    (when (< (.-position.y sprite) 150)
      (set! (.-position.y sprite) (+ sprite-y 0.3)))))

;;(.-position.y sanic)

(def bg-far (PIXI/TilingSprite.
                          (.fromImage PIXI/Texture "img/bg-far.png")
                          512 256))
(def bg-mid (PIXI/TilingSprite.
             (.fromImage PIXI/Texture "img/bg-mid.png")
             512 256))
(def sanic (.fromImage PIXI/Sprite "img/sanic-s.png"))

(defn animate []
  (.tick js/kd)
  (.render renderer stage)
  (apply-gravity sanic)
  (js/requestAnimationFrame animate))


(defn move-left []
  (set! (.-position.x sanic) (- (.-position.x sanic) 1)))

(defn move-right []
  (set! (.-position.x sanic) (+ (.-position.x sanic) 1)))

(defn move-jump []
  (set! (.-position.y sanic) (- (.-position.y sanic) 10)))

(defn init-keys []
  (.down js/kd.LEFT move-left)
  (.down js/kd.RIGHT move-right)
  (.down js/kd.SPACE move-jump)
  )

(defn start-music []
  (let [sound (.createElement js/document "Audio")
        body (.-body js/document)]
    (set! (.-src sound) "mp3/sanic.mp3")
    (.appendChild body sound)
    (.play sound)))

(defn on-tiles-loaded [assets]
  (set! (.-y (.-position sanic)) 150)
  (set! (.-x (.-position sanic)) 10)
  (set! (.-interactive sanic) true)
  (dorun (map #(.addChild game-container %) [bg-far bg-mid sanic]))
  (init-keys)
  (start-music)
  (animate))

(defn setup []
  (.addChild stage game-container)
  (.appendChild app (.-view renderer))
  (set! (.-onComplete loader) on-tiles-loaded)
  (.load loader)
  )

(.addEventListener js/document "DOMContentLoaded" setup)
