(ns data-vis.plot1)

(use '(incanter core processing))

(defn draw-point
  "Draw a circle based on the data passed.
    The radius will vary according to the size of data.
    If the data is large a larger circle will be drawn.
    color parameter will allow for a color circle
    x, y are the coordinates where the circle will be drawn"
  [sktch aes-count x y]
  (let [cir-size (cond
                  (and (>= aes-count 0) (<= aes-count 100)) 10
                  (and (>= aes-count 10) (<= aes-count 300)) 30
                  (and (>= aes-count 301) (<= aes-count 1000)) 50
                  (and (>= aes-count 1001) (<= aes-count 5000)) 70
                  :else 90)
        cir-color (cond
                   (and (>= aes-count 0) (<= aes-count 100)) [250 0 0]
                   (and (>= aes-count 10) (<= aes-count 300)) [0 0 250]
                   (and (>= aes-count 301) (<= aes-count 1000)) [0 250 0]
                   (and (>= aes-count 1001) (<= aes-count 5000)) [100 250 180]
                   :else [50 50 50])
        aes-count aes-count]

    (doto sktch
      (fill 30 130 190)
      ;;(draw-circle x y cir-size cir-color)


      ;;      (cond
      ;; (and (>= aes-count 0) (<= aes-count 100)) (draw-circle1 x y 15)
      ;; (and (>= aes-count 100) (<= aes-count 300)) (draw-circle2 x y 30)
      ;; (and (>= aes-count 301) (<= aes-count 1000)) (draw-circle3 x y 50)
      ;; (and (>= aes-count 1001) (<= aes-count 5000)) (draw-circle3 x y 70)
      ;; :else (draw-circle3 x y 90)
      ;;(if (>= aes-count 0) (draw-circle2 x y cir-size))



      (draw-circle3 x y cir-size)
;;;;      (draw-point1 x y cir-size)
      )

     ;;;; (draw-circle2 x y cir-size)
      )))

(defn draw-point1
  "Draw a circle based on the data passed.
    The radius will vary according to the size of data.
    If the data is large a larger circle will be drawn.
    color parameter will allow for a color circle
    x, y are the coordinates where the circle will be drawn"
  [sktch aes-count x y]
  (doto sktch
    (cond
     (and (>= aes-count 0) (<= aes-count 100)) (draw-circle1 x y 15)
     (and (>= aes-count 10) (<= aes-count 300)) (draw-circle2 x y 30)
     (and (>= aes-count 301) (<= aes-count 1000)) (draw-circle3 x y 50)
     (and (>= aes-count 1001) (<= aes-count 5000)) (draw-circle3 x y 70)
     :else (draw-circle3 x y 90))))


(defn test-fun []
  (println "debug: -----> TEST FUN called *************************")
  true)

(defn test-sketch [sktch]
  (doto sktch
    (fill 190 140 90)
   (ellipse 100 100 100 100)
   (ellipse 400 700 90 90)))


(defn test-fun2 []
  true)

(defn tool-tip [sktch x y message]
  (doto sktch
    (text message x y)))

(defn grid-lines [sktch]
  (doto sktch
    ;; Vertical lines (Y starts from top (0))
    ;;(line 100 0 100 800)
    ;;(line 200 0 200 800)
    (line 300 0 300 650)
    (line 400 0 400 650)
    (line 500 0 500 650)
    (line 600 0 600 650)
    (line 700 0 700 650)

    (line 200 100 800 100)
    (line 200 200 800 200)
    (line 200 300 800 300)
    (line 200 400 800 400)
    (line 200 500 800 500)
    (line 200 600 800 600)))

(defn initialize-plot [sktch]
  (doto sktch
    (background 0) ;; gray
    (fill 255 255 255)
    (stroke 125)

    (grid-lines)))

(defn draw-circle [sktch x y cir-radius cir-color]
  (doto sktch
    (fill (get cir-color 0) (get cir-color 1) (get cir-color 2))
    (ellipse x y cir-radius cir-radius)))

(defn draw-red-circle [sktch x y cir-radius]
  (doto sktch
    (draw-circle x y cir-radius [255 0 0])))

(defn draw-beige-circle [sktch x y cir-radius]
  (doto sktch
    (draw-circle x y cir-radius [242 228 187])))

(defn draw-dark-gray-circle [sktch x y cir-radius]
  (doto sktch
    (draw-circle x y cir-radius [133 133 133])))

(defn draw-circle1 [sktch x y cir-radius]
  (draw-red-circle x y cir-radius))

(defn draw-circle2 [sktch x y cir-radius]
  (doto sktch
    (draw-beige-circle x y (+ cir-radius 12))
    (draw-red-circle x y cir-radius)))

(defn draw-circle3 [sktch x y cir-radius]
  (doto sktch
    (draw-dark-gray-circle x y (+ cir-radius 20))
    (draw-beige-circle x y (+ cir-radius 12))
    (draw-red-circle x y cir-radius)))

(defn draw-graph []
  ;; set up variable references to use in the sketch object
  (let [radius (ref 50.0)
        X (ref nil)
        Y (ref nil)
        nX (ref nil)
        nY (ref nil)
        delay 16

        ;; define a sketch object (i.e. PApplet)
        sktch (sketch

               ;; define the setup function
               (setup []
                      (doto this
                                        ;no-loop
                        (size 800 800)
                        (stroke-weight 1)
                        (framerate 15)
                        smooth)
                      (dosync
                       (ref-set X (/ (width this) 2))
                       (ref-set Y (/ (width this) 2))
                       (ref-set nX @X)
                       (ref-set nY @Y)))

               ;; define the draw function
               (draw []
                     (dosync
                      (ref-set radius (+ @radius (sin (/ (frame-count this) 4))))
                      (ref-set X (+ @X (/ (- @nX @X) delay)))
                      (ref-set Y (+ @Y (/ (- @nY @Y) delay))))
                     (doto this

                       (initialize-plot)

                       ;;(line 200 700 800 700)

                       (text "AVANDIA" 285 670)
                       (text "ACTOS" 385 670)
                       (text "BONIVA" 485 670)
                       (text "PRADAXA" 585 670)

                       (text "Myocardial Infarction" 10 605)
                       (text "Bladder Cancer" 10 505)
                       (text "Cardiac Failure" 10 405)
                       (text "Hemorrhage" 10 305)


                       ;;(ellipse @X @Y @radius @radius)


                       (draw-point 1060 300 600)
                       (draw-point 350 600 300)

                       (println "debug: --->>> CALLING --- draw-data -==============")


                       (draw-point 550 400 500)

                       (draw-point 100 400 400)

                       ;;;;;(fill 190 70 10)

                       (draw-point 90 300 600)

                       (draw-point 180 300 400)

                       (draw-point 98 500 300)

                       (draw-point 80 600 600)

                       (fill 255 255 255)

                       (text "Tool Tip" @X @Y)
                       ;;(test-sketch this)
                    ;;;;   (test-fun2)

;;;;;;;;                       (ellipse 700 200 (draw-data 35 "red" 700 200) (draw-data 35 "red" 700 200))

;;                       (test-sketch)
;;                       (draw-point 140 600 400)

                        ;(save "proc_example1.png")
                       ))




               ;; define mouseMoved function (mouseMoved and mouseDraw
               ;; require a 'mouse-event' argument unlike the standard Processing
               ;; methods)
               (mouseMoved [mouse-event]
                           (dosync
                            ;;(tool-tip nX nY "Something")
                            ;; mouse-x and mouse-y take the mouse-event as an argument
                            (ref-set nX (mouse-x mouse-event))
                            (ref-set nY (mouse-y mouse-event)))))]

    ;; use the view function to display the sketch
    (view sktch :size [800 800])))

(draw-graph)
