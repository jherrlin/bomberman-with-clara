(ns se.jherrlin.server.saved-event-store)


(def saved-event-store
  '({:id #uuid "66c5edc6-17f5-4858-98fb-0ddee8962429",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:19:04.472-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [1 1]}}
     {:id #uuid "05cde73c-bddc-4f77-931e-b64964a2e6cd",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:19:04.472-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [1 3]}}
     {:id #uuid "e730fc16-33db-4b97-b545-26c54f3bc162",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:19:04.472-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [1 4]}}
     {:id #uuid "368c1269-6b61-4d0b-9c3a-019004e9b363",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:19:04.472-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [1 5]}}
     {:id #uuid "4116a88a-6ebf-4a6b-ad0e-e3f98bf670c9",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:19:04.472-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [1 2]}}
     {:id #uuid "ce421914-9302-40bb-b850-604205a84515",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [1 2],
       :fire-start-timestamp #inst "2021-09-15T09:19:02.854-00:00"}}
     {:id #uuid "de22d9d8-ef7f-413e-a664-c7d6b539d15f",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [1 1],
       :fire-start-timestamp #inst "2021-09-15T09:19:02.854-00:00"}}
     {:id #uuid "f0ac1ef3-1587-4ff6-879a-637d25355060",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [1 3],
       :fire-start-timestamp #inst "2021-09-15T09:19:02.854-00:00"}}
     {:id #uuid "53d5ee23-811a-4563-b212-285e753d2ef5",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [1 4],
       :fire-start-timestamp #inst "2021-09-15T09:19:02.854-00:00"}}
     {:id #uuid "d24c2b4a-c60d-4a7b-802c-e07ed1f13ed7",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [1 5],
       :fire-start-timestamp #inst "2021-09-15T09:19:02.854-00:00"}}
     {:id #uuid "e11e3dbb-1cd7-4f6c-9c15-ab539dd50b3c",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "stone-to-remove",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5", :position-xy [1 5]}}
     {:id #uuid "6d4b2c9e-ae5e-447e-9c6b-4da80dbc911a",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-exploading",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :position-xy [1 2],
       :fire-length 3}}
     {:id #uuid "451cc432-6bdb-4897-8d2b-6bdf5cd7ba95",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-to-remove",
      :time #inst "2021-09-15T09:19:02.868-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :bomb-position-xy [1 2]}}
     {:id #uuid "05396791-60e3-47c9-b446-c4672dfd4f95",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:19:00.058-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [1 2],
       :direction :north}}
     {:id #uuid "179e427a-462d-46dd-a9b1-096405992421",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:19:00.058-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [1 1],
       :direction :north}}
     {:id #uuid "bc236ef8-8588-49b7-954b-4e06bca788f4",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-to-add",
      :time #inst "2021-09-15T09:18:59.659-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :bomb-position-xy [1 2],
       :fire-length 3,
       :bomb-added-timestamp #inst "2021-09-15T09:18:59.654-00:00"}}
     {:id #uuid "1a2b4435-264b-4ba3-a0f5-19c75aa62299",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-place-bomb",
      :time #inst "2021-09-15T09:18:59.659-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [1 2],
       :fire-length 3,
       :timestamp #inst "2021-09-15T09:18:59.654-00:00",
       :max-nr-of-bombs-for-player 3}}
     {:id #uuid "5d5c78db-5ae2-4db5-ab03-d2cea37ad2a1",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:18:59.058-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [1 1],
       :direction :south}}
     {:id #uuid "aaa7ebdd-b8ed-4a01-8ab6-13511c652911",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:18:59.058-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [1 2],
       :direction :south}}
     {:id #uuid "77f66e5c-02f6-4523-af61-b11d8bf1d3af",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:18:58.659-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [2 1],
       :direction :west}}
     {:id #uuid "81a39971-9edc-44a2-8069-63f14cc04cb6",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:18:58.659-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [1 1],
       :direction :west}}
     {:id #uuid "dd68e7da-0632-41c1-8b64-88adf54de13c",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:18:58.459-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 1],
       :direction :west}}
     {:id #uuid "2eb770d1-e525-4a38-bee8-f26a1a76739b",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:18:58.459-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [2 1],
       :direction :west}}
     {:id #uuid "661d9f8e-5586-4b17-a743-aea64991dc7a",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:18:58.066-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [3 1],
       :direction :west}}
     {:id #uuid "4c8085c3-f546-439c-86c4-ab2db8025bb9",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:18:58.065-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [4 1],
       :direction :west}}
     {:id #uuid "de53962f-2b36-4005-be72-296e032beb5c",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.690-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [6 3]}}
     {:id #uuid "0bf09737-33ff-4b03-b5bc-975fec7590c9",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [3 4]}}
     {:id #uuid "72e534ae-46dd-4c75-8420-bae2dd99cdef",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [3 3]}}
     {:id #uuid "ebe4e43b-0673-43ab-acc4-ab6f0c8239e6",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [3 2]}}
     {:id #uuid "c4204911-dc8d-4945-9a06-cf872ff097b9",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [3 1]}}
     {:id #uuid "385bb4b6-33d7-4120-b9e3-68f1279d8cd2",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [2 3]}}
     {:id #uuid "d08a662b-e4df-4984-8789-2c2a1af6b98b",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [1 3]}}
     {:id #uuid "db79f772-2762-4fc8-aa32-c1c229587675",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [4 3]}}
     {:id #uuid "ed1f85e9-c71a-403b-a8ec-a2d28db2d08a",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:44.689-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [5 3]}}
     {:id #uuid "8a46c336-7aa2-4502-a594-94b4fb2442b5",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.083-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [3 4],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "00ce9daa-6f6c-4d13-85e9-bb896858150b",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "stone-to-remove",
      :time #inst "2021-09-15T09:16:43.083-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5", :position-xy [3 4]}}
     {:id #uuid "c9b07667-e45e-4ddf-9d2e-cc7def950d1b",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-exploading",
      :time #inst "2021-09-15T09:16:43.083-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :position-xy [3 3],
       :fire-length 3}}
     {:id #uuid "0dc2c592-032b-4105-aa5b-5557232a58e1",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-to-remove",
      :time #inst "2021-09-15T09:16:43.083-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :bomb-position-xy [3 3]}}
     {:id #uuid "3d85ecd2-994e-49e6-9570-97767f8fdd42",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [3 3],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "a57dda82-876e-4822-bf2f-90d054f264ac",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [3 2],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "af9d6f6b-cb22-4e8f-a265-d7c4fac2dda3",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [3 1],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "fb526127-dc0e-47a2-af6d-ad965009dd9d",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [2 3],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "8cc47c40-db15-4555-9ff8-0c1d0f2e03b2",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [1 3],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "ee98e5cd-06ff-466f-82e3-3ccdaa0d37fd",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [4 3],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "d0439e7a-c5f6-4da6-a89e-b3b9b365ab10",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [5 3],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "b4b6cb1e-5c90-4711-a5e3-97fa8f909bba",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:43.082-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [6 3],
       :fire-start-timestamp #inst "2021-09-15T09:16:43.055-00:00"}}
     {:id #uuid "efced585-ccc3-4304-9406-fbcb2ca5d9ee",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:41.055-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 1],
       :direction :east}}
     {:id #uuid "8938245d-863d-48a7-832c-2d9c8f8efe81",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:41.055-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [4 1],
       :direction :east}}
     {:id #uuid "19a0c54a-6a37-4b13-8858-8463ef05dd36",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:40.662-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 2],
       :direction :north}}
     {:id #uuid "416b9368-9416-4838-b3a7-c8e7b3a40d75",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:40.662-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [3 1],
       :direction :north}}
     {:id #uuid "7ade6351-59a9-4def-9f0b-c7d46cc17d07",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:40.460-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 3],
       :direction :north}}
     {:id #uuid "d0a7ab79-b866-4226-bda0-d899ab6ef75e",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:40.460-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [3 2],
       :direction :north}}
     {:id #uuid "e5c488a3-e521-4c35-aeb4-92b3ccd615d2",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-to-add",
      :time #inst "2021-09-15T09:16:40.062-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :bomb-position-xy [3 3],
       :fire-length 3,
       :bomb-added-timestamp #inst "2021-09-15T09:16:40.054-00:00"}}
     {:id #uuid "7feebd2b-a0ae-41d4-92c4-f7ac65264cda",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-place-bomb",
      :time #inst "2021-09-15T09:16:40.062-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 3],
       :fire-length 3,
       :timestamp #inst "2021-09-15T09:16:40.054-00:00",
       :max-nr-of-bombs-for-player 3}}
     {:id #uuid "bf969821-9935-47f1-a68d-26ccc3efc27b",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:39.459-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [2 3],
       :direction :east}}
     {:id #uuid "28fdbc66-12ca-44f3-b915-22885849d52b",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:39.459-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [3 3],
       :direction :east}}
     {:id #uuid "93b02836-6afd-46ae-be40-70e7f3967ca7",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:39.059-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [2 3],
       :direction :east}}
     {:id #uuid "784b38e7-a480-4029-abb6-f82abbe9e3ca",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:39.058-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [1 3],
       :direction :east}}
     {:id #uuid "e1f24f2d-f854-47b9-8061-338648252940",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:38.455-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [1 2],
       :direction :south}}
     {:id #uuid "fbd72ad7-eeba-4a4f-bfb1-e916e30036db",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:38.455-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [1 3],
       :direction :south}}
     {:id #uuid "6cb4369a-644d-4e04-98d0-0cfae3effb16",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:37.891-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [2 1]}}
     {:id #uuid "947f0279-3e34-4f38-8a38-0bb522c94621",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:37.891-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [1 1]}}
     {:id #uuid "4f1da81c-f320-4afa-badf-8622ace40776",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:37.891-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [4 1]}}
     {:id #uuid "2f13949b-1ccc-46f5-94ce-07c2bbd51bd8",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:37.891-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [5 1]}}
     {:id #uuid "6181cc51-d38c-4969-9d5e-3a263b83222b",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:37.891-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [3 2]}}
     {:id #uuid "fa1f03dd-4c12-46e0-b334-4b3bc5949e49",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:37.891-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [3 3]}}
     {:id #uuid "1a856253-ec8d-4bc0-949f-0493efad92f5",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-remove",
      :time #inst "2021-09-15T09:16:37.885-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire-position-xy [3 1]}}
     {:id #uuid "b1a6c61c-2c30-4d88-9b29-57839f132f3a",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-to-remove",
      :time #inst "2021-09-15T09:16:36.295-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :bomb-position-xy [3 1]}}
     {:id #uuid "dd2c2f46-0a44-4710-b5ad-3119589ac5f3",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "stone-to-remove",
      :time #inst "2021-09-15T09:16:36.292-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5", :position-xy [5 1]}}
     {:id #uuid "af74c016-0576-4925-aca7-368687f08faa",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-exploading",
      :time #inst "2021-09-15T09:16:36.292-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :position-xy [3 1],
       :fire-length 3}}
     {:id #uuid "7f55ebeb-e870-4be1-a5cc-d51232edafe1",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:36.291-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [2 1],
       :fire-start-timestamp #inst "2021-09-15T09:16:36.254-00:00"}}
     {:id #uuid "64e166e3-b7fe-486d-8466-fb9f8375b54c",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:36.291-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [1 1],
       :fire-start-timestamp #inst "2021-09-15T09:16:36.254-00:00"}}
     {:id #uuid "9fb0604b-7eac-4e9a-9925-fd777b4a567a",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:36.291-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [4 1],
       :fire-start-timestamp #inst "2021-09-15T09:16:36.254-00:00"}}
     {:id #uuid "85b0dc02-8f7d-4fdd-b65c-29dc05a5646d",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:36.291-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [5 1],
       :fire-start-timestamp #inst "2021-09-15T09:16:36.254-00:00"}}
     {:id #uuid "fb4ce363-0d6b-4b9c-9ff7-0d0a5725dcd6",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:36.291-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [3 2],
       :fire-start-timestamp #inst "2021-09-15T09:16:36.254-00:00"}}
     {:id #uuid "55c1f4a6-7275-47c2-bba5-c7722d89f8df",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:36.291-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [3 3],
       :fire-start-timestamp #inst "2021-09-15T09:16:36.254-00:00"}}
     {:id #uuid "d40f51ce-8e4c-4631-b442-890ab6222e78",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "stone-to-remove",
      :time #inst "2021-09-15T09:16:36.291-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5", :position-xy [3 3]}}
     {:id #uuid "7e1f1db8-a626-42cf-bbcd-a82f5ca20b38",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "fire-to-add",
      :time #inst "2021-09-15T09:16:36.289-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :fire-position-xy [3 1],
       :fire-start-timestamp #inst "2021-09-15T09:16:36.254-00:00"}}
     {:id #uuid "4ed56ca2-9ee6-4768-a50f-006d3bc3c80d",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:34.660-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [1 1],
       :direction :south}}
     {:id #uuid "97789530-93f5-4e79-9649-6612a49791de",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:34.660-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [1 2],
       :direction :south}}
     {:id #uuid "ede480dd-6a72-43bb-a01d-c04d4401a247",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:34.260-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [2 1],
       :direction :west}}
     {:id #uuid "2287e519-4893-48c8-b363-7dcac34f605f",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:34.260-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [1 1],
       :direction :west}}
     {:id #uuid "9bc790c0-b753-46bd-b001-1f579b318ec6",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:33.461-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 1],
       :direction :west}}
     {:id #uuid "b551d6e2-19eb-455d-af91-5b199424b081",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:33.461-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [2 1],
       :direction :west}}
     {:id #uuid "1198cfd0-96fb-42c8-b124-7a168eea3cc6",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-place-bomb",
      :time #inst "2021-09-15T09:16:33.092-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 1],
       :fire-length 3,
       :timestamp #inst "2021-09-15T09:16:33.055-00:00",
       :max-nr-of-bombs-for-player 3}}
     {:id #uuid "76e98fad-3e61-42d1-9404-43a50d3f418a",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "bomb-to-add",
      :time #inst "2021-09-15T09:16:33.090-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :bomb-position-xy [3 1],
       :fire-length 3,
       :bomb-added-timestamp #inst "2021-09-15T09:16:33.055-00:00"}}
     {:id #uuid "02fa7026-22c4-4928-bfa2-2137b97307e3",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:31.860-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 2],
       :direction :north}}
     {:id #uuid "0f30e83f-2117-4e14-a494-7d29e810cf49",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:31.860-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [3 1],
       :direction :north}}
     {:id #uuid "22c676ad-9fa0-440d-9f0a-758f8e631991",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:31.060-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [3 1],
       :direction :south}}
     {:id #uuid "1b92d719-f58c-4625-aaee-fee1c44ce8eb",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:31.060-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [3 2],
       :direction :south}}
     {:id #uuid "837c424a-2200-47b9-a757-bc720cf48e60",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:28.659-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [2 1],
       :direction :east}}
     {:id #uuid "7fa1568f-4940-40f4-a85a-c440aa7b2068",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:28.659-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [3 1],
       :direction :east}}
     {:id #uuid "c54aeb69-191f-4088-9481-f3af2be91f87",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:28.060-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :next-position [2 1],
       :direction :east}}
     {:id #uuid "81e733a2-9e8d-471f-99e3-60dbd404ebf4",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:28.059-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :current-xy [1 1],
       :direction :east}}
     {:id #uuid "d25d664c-de5e-4187-8640-ef80e1ea8da9",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:25.459-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "74c1d47b-658f-43f4-b7e2-140802aca40a",
       :current-xy [1 2],
       :direction :south}}
     {:id #uuid "4c6349d3-5c06-4e4b-8b29-fa5fb0101e0f",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:25.459-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "74c1d47b-658f-43f4-b7e2-140802aca40a",
       :next-position [1 3],
       :direction :south}}
     {:id #uuid "7b98153f-2997-4d06-927b-00827dbd9d34",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "move",
      :time #inst "2021-09-15T09:16:24.876-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "74c1d47b-658f-43f4-b7e2-140802aca40a",
       :next-position [1 2],
       :direction :south}}
     {:id #uuid "d142014a-56a9-4dcf-a39c-7dc545368bc4",
      :source "urn:se:jherrlin:bomberman:player",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "wants-to-move",
      :time #inst "2021-09-15T09:16:24.872-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "74c1d47b-658f-43f4-b7e2-140802aca40a",
       :current-xy [1 1],
       :direction :south}}
     {:id #uuid "6d5d3bc3-e7b9-407c-8060-82b6da6e5c95",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "join-game",
      :time #inst "2021-09-15T09:16:22.815-00:00",
      :content-type "application/edn",
      :data
      {:user-facing-direction :south,
       :max-nr-of-bombs-for-user 3,
       :position [1 1],
       :fire-length 3,
       :game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "74c1d47b-658f-43f4-b7e2-140802aca40a",
       :player-name "Hannah"}}
     {:id #uuid "bdff56eb-8c24-44f9-aba7-b3a2a5474cdd",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "join-game",
      :time #inst "2021-09-15T09:16:10.237-00:00",
      :content-type "application/edn",
      :data
      {:user-facing-direction :south,
       :max-nr-of-bombs-for-user 3,
       :position [1 1],
       :fire-length 3,
       :game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :player-id #uuid "07ff22b5-7bee-49ab-bf6a-38faeebbabb5",
       :player-name "John"}}
     {:id #uuid "5e757003-f69b-496d-aa26-9809ee6cf68f",
      :source "urn:se:jherrlin:bomberman:game",
      :subject #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
      :type "create-game",
      :time #inst "2021-09-15T09:15:58.742-00:00",
      :content-type "application/edn",
      :data
      {:game-id #uuid "f4e152ca-cd47-4f67-9015-9bfecc272bf5",
       :fire (),
       :stones [[1 5] [2 5] [3 4] [3 3] [5 1]],
       :password "pwd",
       :game-name "JohnsGame",
       :game-state :created,
       :flying-bombs (),
       :game-password "pwd",
       :bombs (),
       :board
       [[{:type :wall, :x 0, :y 0}
         {:type :wall, :x 1, :y 0}
         {:type :wall, :x 2, :y 0}
         {:type :wall, :x 3, :y 0}
         {:type :wall, :x 4, :y 0}
         {:type :wall, :x 5, :y 0}
         {:type :wall, :x 6, :y 0}
         {:type :wall, :x 7, :y 0}
         {:type :wall, :x 8, :y 0}
         {:type :wall, :x 9, :y 0}
         {:type :wall, :x 10, :y 0}
         {:type :wall, :x 11, :y 0}
         {:type :wall, :x 12, :y 0}
         {:type :wall, :x 13, :y 0}
         {:type :wall, :x 14, :y 0}
         {:type :wall, :x 15, :y 0}
         {:type :wall, :x 16, :y 0}
         {:type :wall, :x 17, :y 0}
         {:type :wall, :x 18, :y 0}]
        [{:type :wall, :x 0, :y 1}
         {:type :floor, :x 1, :y 1}
         {:type :floor, :x 2, :y 1}
         {:type :floor, :x 3, :y 1}
         {:type :floor, :x 4, :y 1}
         {:type :floor, :x 5, :y 1}
         {:type :floor, :x 6, :y 1}
         {:type :floor, :x 7, :y 1}
         {:type :floor, :x 8, :y 1}
         {:type :floor, :x 9, :y 1}
         {:type :floor, :x 10, :y 1}
         {:type :floor, :x 11, :y 1}
         {:type :floor, :x 12, :y 1}
         {:type :floor, :x 13, :y 1}
         {:type :floor, :x 14, :y 1}
         {:type :floor, :x 15, :y 1}
         {:type :floor, :x 16, :y 1}
         {:type :floor, :x 17, :y 1}
         {:type :wall, :x 18, :y 1}]
        [{:type :wall, :x 0, :y 2}
         {:type :floor, :x 1, :y 2}
         {:type :wall, :x 2, :y 2}
         {:type :floor, :x 3, :y 2}
         {:type :wall, :x 4, :y 2}
         {:type :floor, :x 5, :y 2}
         {:type :wall, :x 6, :y 2}
         {:type :floor, :x 7, :y 2}
         {:type :wall, :x 8, :y 2}
         {:type :floor, :x 9, :y 2}
         {:type :wall, :x 10, :y 2}
         {:type :floor, :x 11, :y 2}
         {:type :wall, :x 12, :y 2}
         {:type :floor, :x 13, :y 2}
         {:type :wall, :x 14, :y 2}
         {:type :floor, :x 15, :y 2}
         {:type :wall, :x 16, :y 2}
         {:type :floor, :x 17, :y 2}
         {:type :wall, :x 18, :y 2}]
        [{:type :wall, :x 0, :y 3}
         {:type :floor, :x 1, :y 3}
         {:type :floor, :x 2, :y 3}
         {:type :floor, :x 3, :y 3}
         {:type :floor, :x 4, :y 3}
         {:type :floor, :x 5, :y 3}
         {:type :floor, :x 6, :y 3}
         {:type :floor, :x 7, :y 3}
         {:type :floor, :x 8, :y 3}
         {:type :floor, :x 9, :y 3}
         {:type :floor, :x 10, :y 3}
         {:type :floor, :x 11, :y 3}
         {:type :floor, :x 12, :y 3}
         {:type :floor, :x 13, :y 3}
         {:type :floor, :x 14, :y 3}
         {:type :floor, :x 15, :y 3}
         {:type :floor, :x 16, :y 3}
         {:type :floor, :x 17, :y 3}
         {:type :wall, :x 18, :y 3}]
        [{:type :wall, :x 0, :y 4}
         {:type :floor, :x 1, :y 4}
         {:type :wall, :x 2, :y 4}
         {:type :floor, :x 3, :y 4}
         {:type :wall, :x 4, :y 4}
         {:type :floor, :x 5, :y 4}
         {:type :wall, :x 6, :y 4}
         {:type :floor, :x 7, :y 4}
         {:type :wall, :x 8, :y 4}
         {:type :floor, :x 9, :y 4}
         {:type :wall, :x 10, :y 4}
         {:type :floor, :x 11, :y 4}
         {:type :wall, :x 12, :y 4}
         {:type :floor, :x 13, :y 4}
         {:type :wall, :x 14, :y 4}
         {:type :floor, :x 15, :y 4}
         {:type :wall, :x 16, :y 4}
         {:type :floor, :x 17, :y 4}
         {:type :wall, :x 18, :y 4}]
        [{:type :wall, :x 0, :y 5}
         {:type :floor, :x 1, :y 5}
         {:type :floor, :x 2, :y 5}
         {:type :floor, :x 3, :y 5}
         {:type :floor, :x 4, :y 5}
         {:type :floor, :x 5, :y 5}
         {:type :floor, :x 6, :y 5}
         {:type :floor, :x 7, :y 5}
         {:type :floor, :x 8, :y 5}
         {:type :floor, :x 9, :y 5}
         {:type :floor, :x 10, :y 5}
         {:type :floor, :x 11, :y 5}
         {:type :floor, :x 12, :y 5}
         {:type :floor, :x 13, :y 5}
         {:type :floor, :x 14, :y 5}
         {:type :floor, :x 15, :y 5}
         {:type :floor, :x 16, :y 5}
         {:type :floor, :x 17, :y 5}
         {:type :wall, :x 18, :y 5}]
        [{:type :wall, :x 0, :y 6}
         {:type :floor, :x 1, :y 6}
         {:type :wall, :x 2, :y 6}
         {:type :floor, :x 3, :y 6}
         {:type :wall, :x 4, :y 6}
         {:type :floor, :x 5, :y 6}
         {:type :wall, :x 6, :y 6}
         {:type :floor, :x 7, :y 6}
         {:type :wall, :x 8, :y 6}
         {:type :floor, :x 9, :y 6}
         {:type :wall, :x 10, :y 6}
         {:type :floor, :x 11, :y 6}
         {:type :wall, :x 12, :y 6}
         {:type :floor, :x 13, :y 6}
         {:type :wall, :x 14, :y 6}
         {:type :floor, :x 15, :y 6}
         {:type :wall, :x 16, :y 6}
         {:type :floor, :x 17, :y 6}
         {:type :wall, :x 18, :y 6}]
        [{:type :wall, :x 0, :y 7}
         {:type :floor, :x 1, :y 7}
         {:type :floor, :x 2, :y 7}
         {:type :floor, :x 3, :y 7}
         {:type :floor, :x 4, :y 7}
         {:type :floor, :x 5, :y 7}
         {:type :floor, :x 6, :y 7}
         {:type :floor, :x 7, :y 7}
         {:type :floor, :x 8, :y 7}
         {:type :floor, :x 9, :y 7}
         {:type :floor, :x 10, :y 7}
         {:type :floor, :x 11, :y 7}
         {:type :floor, :x 12, :y 7}
         {:type :floor, :x 13, :y 7}
         {:type :floor, :x 14, :y 7}
         {:type :floor, :x 15, :y 7}
         {:type :floor, :x 16, :y 7}
         {:type :floor, :x 17, :y 7}
         {:type :wall, :x 18, :y 7}]
        [{:type :wall, :x 0, :y 8}
         {:type :floor, :x 1, :y 8}
         {:type :wall, :x 2, :y 8}
         {:type :floor, :x 3, :y 8}
         {:type :wall, :x 4, :y 8}
         {:type :floor, :x 5, :y 8}
         {:type :wall, :x 6, :y 8}
         {:type :floor, :x 7, :y 8}
         {:type :wall, :x 8, :y 8}
         {:type :floor, :x 9, :y 8}
         {:type :wall, :x 10, :y 8}
         {:type :floor, :x 11, :y 8}
         {:type :wall, :x 12, :y 8}
         {:type :floor, :x 13, :y 8}
         {:type :wall, :x 14, :y 8}
         {:type :floor, :x 15, :y 8}
         {:type :wall, :x 16, :y 8}
         {:type :floor, :x 17, :y 8}
         {:type :wall, :x 18, :y 8}]
        [{:type :wall, :x 0, :y 9}
         {:type :floor, :x 1, :y 9}
         {:type :floor, :x 2, :y 9}
         {:type :floor, :x 3, :y 9}
         {:type :floor, :x 4, :y 9}
         {:type :floor, :x 5, :y 9}
         {:type :floor, :x 6, :y 9}
         {:type :floor, :x 7, :y 9}
         {:type :floor, :x 8, :y 9}
         {:type :floor, :x 9, :y 9}
         {:type :floor, :x 10, :y 9}
         {:type :floor, :x 11, :y 9}
         {:type :floor, :x 12, :y 9}
         {:type :floor, :x 13, :y 9}
         {:type :floor, :x 14, :y 9}
         {:type :floor, :x 15, :y 9}
         {:type :floor, :x 16, :y 9}
         {:type :floor, :x 17, :y 9}
         {:type :wall, :x 18, :y 9}]
        [{:type :wall, :x 0, :y 10}
         {:type :wall, :x 1, :y 10}
         {:type :wall, :x 2, :y 10}
         {:type :wall, :x 3, :y 10}
         {:type :wall, :x 4, :y 10}
         {:type :wall, :x 5, :y 10}
         {:type :wall, :x 6, :y 10}
         {:type :wall, :x 7, :y 10}
         {:type :wall, :x 8, :y 10}
         {:type :wall, :x 9, :y 10}
         {:type :wall, :x 10, :y 10}
         {:type :wall, :x 11, :y 10}
         {:type :wall, :x 12, :y 10}
         {:type :wall, :x 13, :y 10}
         {:type :wall, :x 14, :y 10}
         {:type :wall, :x 15, :y 10}
         {:type :wall, :x 16, :y 10}
         {:type :wall, :x 17, :y 10}
         {:type :wall, :x 18, :y 10}]]}}))


(def events2
  '({:id #uuid "5328f93f-c9d2-4169-b44f-518c4516a9d4",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:43.944-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy nil,
      :direction :south}}
    {:id #uuid "7672d68b-958c-4bcf-8e67-d911dcdb81b6",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:43.789-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [15 3],
      :fire-length 4}}
    {:id #uuid "87a0f9d3-d40e-4852-afe2-17c3a0448f78",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:43.789-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [15 5],
      :fire-length 4}}
    {:id #uuid "2409b496-ccf6-499c-9333-90ff1e47e434",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:43.789-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [15 1],
      :fire-length 4}}
    {:id #uuid "c4c0003a-3214-4c1a-b319-b60ac454bf53",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:43.789-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 3]}}
    {:id #uuid "de218d72-17a9-424a-a92b-a42e6ec5b66b",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:43.789-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 5]}}
    {:id #uuid "13c89706-eb3d-4af7-8b38-045b7490eb5c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:43.789-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 1]}}
    {:id #uuid "b0075389-4200-42c9-8dbb-db43ade6ddc2",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:43.786-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [13 5]}}
    {:id #uuid "a90ef6d8-fee9-42a4-89d8-5acd41427617",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:43.786-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [14 1]}}
    {:id #uuid "853c3d9c-a51b-46f5-9bb7-dff6f3c5a078",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "end",
     :time #inst "2021-09-15T17:08:43.786-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :winner #uuid "bb6c0540-1cec-48d7-9b08-15427be67011"}}
    {:id #uuid "2ce53acd-f446-455f-bef1-094c8e1a54d3",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "bd600103-6c29-4679-b8c2-cb7383d2061b",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "974be4ed-b278-4574-a98d-4fd24f492f46",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [14 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "d2a6cd28-e9bf-44c4-828f-c08f4a4d9675",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [16 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "38db9e73-1d7b-4eee-b68b-d89698df5026",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [17 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "aa6451e1-786b-4837-bfb2-affec8fca673",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 2],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "f64b851c-b662-495a-ba73-cf33b68f3335",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "b9ef1fe1-7f3f-48fe-8a76-789f04234b90",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 4],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "70a3d959-268e-4cf6-b670-0387df69ace8",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "da47f765-c4eb-4860-a089-658f6ad422bf",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:43.785-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [17 5]}}
    {:id #uuid "d112d3c9-f02d-4df1-a881-105d4cdae860",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "c55653ef-9c7d-43f0-a368-ad8f7ffedb41",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 6],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "f721b5ce-ee1f-4ec6-bdd7-de8b69b63035",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "875f8f01-f801-4cdd-869c-3b25489e6a86",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 4],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "2a45c6a7-a4e8-4b4e-90a6-2948fa785366",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [17 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "d388208a-4d3c-45cd-a84e-3ff5f7315407",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [16 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "aab91755-42e3-4445-b3b8-7a8a45716ed9",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [11 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "7f071fcf-c76c-44c4-8850-0c008015c5f0",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [12 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "c4e0bf80-6c99-4387-aee6-913a3be3d149",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [13 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "37a226a8-c442-462d-9ceb-ac3e784bb36e",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [14 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "59e1c13e-cdd8-4c3e-a924-296615c2273c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "9ef5e881-c4dc-4fd4-bcf0-1166313659ec",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 2],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "c9ba256e-e141-4713-be85-9bc9f5675383",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "a61272ea-155c-47c0-9717-887850348005",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 9],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "8641aee3-5201-4cd1-9e39-a057a1b1adb1",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 8],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "44ea69eb-dac6-4307-9d25-9f96a5a4383f",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "e1a99bd9-55a3-4832-85e7-a9642862bacf",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 6],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "b5499b00-2935-4bb4-aeb7-4992b9edfb46",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [17 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "d863422a-ecf1-47da-8123-20f51321f5c5",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [16 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "5015da26-2128-400f-b33a-d0e5cd695af1",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [13 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "4d83382d-de94-4f83-b22c-d51d7c4c8f0c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [14 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "68a90753-9b24-45aa-a7a1-a94f7e4b00ad",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "1c9dee29-fc9f-4f99-8dc6-80aef8b6b0fc",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 2],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "01e96355-691a-4f13-a82c-4fa8250e7e68",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "3215500e-6225-4b9f-9ee8-a39e3a4bdb39",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:43.784-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 4],
      :fire-start-timestamp #inst "2021-09-15T17:08:43.742-00:00"}}
    {:id #uuid "5ee522bd-941b-491a-a151-5dae94537785",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:43.352-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 7],
      :direction :south}}
    {:id #uuid "499c4736-9a77-42b3-bc5d-8b32cbfedf74",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:43.352-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 8],
      :direction :south}}
    {:id #uuid "f2855bf7-99be-45c6-a254-ae6f40554f53",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:43.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 6],
      :direction :south}}
    {:id #uuid "bb88295c-92e2-4ae1-abde-008a6e3bfa91",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:43.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 7],
      :direction :south}}
    {:id #uuid "740fb23d-453f-49a9-842b-c556d600ac03",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:42.950-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 6],
      :direction :west}}
    {:id #uuid "cfcaedc1-b5c1-40e2-a9d0-af3d46d84949",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:42.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 6],
      :direction :west}}
    {:id #uuid "d3527dca-4d42-4099-ad2f-371fef6fadb9",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:42.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :direction :south}}
    {:id #uuid "d83eb319-a2ff-42d6-a925-954e0bf5c7bf",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:42.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 6],
      :direction :south}}
    {:id #uuid "a9e36256-5902-4321-8418-da3cea1ad9db",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:42.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 5],
      :fire-length 4,
      :bomb-added-timestamp #inst "2021-09-15T17:08:42.143-00:00"}}
    {:id #uuid "7f33ffb5-a005-4673-a771-ccfee3722fc7",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:42.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :fire-length 4,
      :timestamp #inst "2021-09-15T17:08:42.143-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "54b32d66-15d2-451a-ba25-f21b57bb25b4",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:41.753-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 5],
      :direction :south}}
    {:id #uuid "5f826464-5582-45c0-83ec-ae87ac1f6c24",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:41.752-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 4],
      :direction :south}}
    {:id #uuid "3b357346-b8b7-4218-b833-21aba7c7a036",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:41.553-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :direction :south}}
    {:id #uuid "b85a9fe1-7909-4015-933a-a044a964f4ad",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:41.553-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 4],
      :direction :south}}
    {:id #uuid "61878c02-cdea-4c9a-b431-8365ffebb5dc",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:41.355-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 3],
      :fire-length 4,
      :bomb-added-timestamp #inst "2021-09-15T17:08:41.343-00:00"}}
    {:id #uuid "a0d15c03-0df5-499a-861e-550559f52c96",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:41.355-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :fire-length 4,
      :timestamp #inst "2021-09-15T17:08:41.343-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "a2811986-3190-4681-bbe2-f2075716e265",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:40.953-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 2],
      :direction :south}}
    {:id #uuid "47eaf029-3506-4c29-b72b-d09c0fc41858",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:40.953-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 3],
      :direction :south}}
    {:id #uuid "5b38d071-0b66-41f5-85a0-55b203daeb86",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:40.750-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :direction :south}}
    {:id #uuid "7bb1d173-68f5-48bc-bd48-4c63e2c4839f",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:40.750-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 2],
      :direction :south}}
    {:id #uuid "9685fec9-0b22-4a3c-990b-00a2bb141d60",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:40.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 1],
      :fire-length 4,
      :bomb-added-timestamp #inst "2021-09-15T17:08:40.543-00:00"}}
    {:id #uuid "cbe947cd-bb9c-4b93-a5da-fc7dcb79efbb",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:40.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :fire-length 4,
      :timestamp #inst "2021-09-15T17:08:40.543-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "b9afa674-1e86-44f1-a001-0456647ed20a",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:40.149-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 2],
      :direction :north}}
    {:id #uuid "ead35fac-7254-4655-b403-9dd9ab66a9b5",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:40.149-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 1],
      :direction :north}}
    {:id #uuid "cd72414c-a72c-4591-9308-f5146966940b",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:39.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :direction :north}}
    {:id #uuid "678b3faf-53b2-4b56-9a0f-726e947cbe0a",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:39.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 2],
      :direction :north}}
    {:id #uuid "b6a09aa7-b4bc-4dac-8050-55007078e2e7",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:39.348-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [14 3],
      :direction :east}}
    {:id #uuid "cf868b43-2a4d-4b5f-98d4-89e94fcb7028",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:39.348-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 3],
      :direction :east}}
    {:id #uuid "270b56e4-12df-472f-9e54-5bfb322bb403",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "picks-fire-inc-item",
     :time #inst "2021-09-15T17:08:38.743-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :item-position-xy [14 3],
      :new-fire-length 4}}
    {:id #uuid "aa50ba89-c249-42a3-b00d-9932ad244e77",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:38.544-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :direction :west}}
    {:id #uuid "3f3c7e10-2e54-4326-b34f-7e4713c29205",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:38.544-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [14 3],
      :direction :west}}
    {:id #uuid "86f57a54-5767-4b7d-a10b-3d5b211bb193",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:38.349-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 3],
      :direction :south}}
    {:id #uuid "7b14b2a9-f1f9-4207-b6e5-e6ac1105d91e",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:38.348-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 2],
      :direction :south}}
    {:id #uuid "e21bcd62-7c56-45f2-9823-91b33be9d9e4",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:37.951-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 2],
      :direction :west}}
    {:id #uuid "4cdb286d-3e98-4940-b5cb-edb41a8f2779",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:37.748-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 2],
      :direction :north}}
    {:id #uuid "fe86b8ed-30cd-4c6b-a7a5-abb1619599dc",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:37.747-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :direction :north}}
    {:id #uuid "2ddaf0f0-5679-48f3-b94d-2494bc90d1c1",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:37.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 3],
      :direction :north}}
    {:id #uuid "40d4bfc1-7831-4852-81e8-dd8f7216a07c",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:37.549-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 4],
      :direction :north}}
    {:id #uuid "47b8719f-1745-4c95-b174-9edb1b0e004d",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:37.349-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :direction :north}}
    {:id #uuid "581efa51-0d12-4f75-b31e-b44111c3088b",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:37.349-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 4],
      :direction :north}}
    {:id #uuid "c92df293-b848-414c-8068-6eef3da157fc",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:37.148-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 5],
      :direction :east}}
    {:id #uuid "3089124e-08eb-4c50-ae25-5356ceaff802",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:37.147-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [14 5],
      :direction :east}}
    {:id #uuid "472fba26-182a-45f5-a338-2b4fc5711274",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 6]}}
    {:id #uuid "93effed7-f475-47b0-82f8-880af6d4d441",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 3]}}
    {:id #uuid "0e3641db-2078-4066-8c69-ec07054c1f68",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 2]}}
    {:id #uuid "af92d7ed-89b4-46af-a42f-5ad2e02137e3",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [14 3]}}
    {:id #uuid "e4d8cf89-78a5-4c38-b694-299c9c17a06d",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [16 3]}}
    {:id #uuid "fb6a8d6a-bcd3-417c-b7ea-b456b7fe0d01",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [17 3]}}
    {:id #uuid "0f084071-d1f9-4325-8d02-e394cf33d89f",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 4]}}
    {:id #uuid "95a5d096-3e9a-4a2f-a3c2-f7e92008b282",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:36.573-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 5]}}
    {:id #uuid "40d89472-e33d-4aad-b1d6-50be9f98a71f",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "c70b4971-1467-4984-98dd-636e6b68d51f",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 2],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "a9e44602-e39d-4250-be0b-1ac01b55b1b2",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [14 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "05bfb0a7-c8fc-453b-a0d2-794554fd0398",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [16 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "2b1b2719-180b-42be-827e-dcfc31cab3f2",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [17 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "aad0fae2-c23e-4e72-9e1e-e967c0ae939a",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 4],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "fb63d707-3e94-473a-ac3f-3bbfa5f3fca4",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "1f771579-442e-45bb-b8ef-bc47b6a39a75",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 6],
      :fire-start-timestamp #inst "2021-09-15T17:08:34.943-00:00"}}
    {:id #uuid "fc5e1abf-11ef-4497-b599-4770c2735028",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [15 2]}}
    {:id #uuid "42fb9a73-453e-4a50-809e-653acd855864",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [14 3]}}
    {:id #uuid "0bdc4d73-db2b-48a5-940b-6f4bd7d7cd1c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [15 3],
      :fire-length 3}}
    {:id #uuid "24275a85-96e1-4858-9a00-b8e9108c395d",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:34.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 3]}}
    {:id #uuid "bf976e0e-c11a-4d62-b50c-7f30dbb8b2ce",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:32.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [11 2]}}
    {:id #uuid "ab6011b7-84ee-4f98-8846-d7966500908e",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:32.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [11 1]}}
    {:id #uuid "97027474-15ae-4326-99c1-7374f98d272a",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:32.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [10 1]}}
    {:id #uuid "b391112b-1e64-42c8-826e-df5ff30dbf34",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:32.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [9 1]}}
    {:id #uuid "0f55fbef-8bb9-4dc5-8aee-4436ce2d61ad",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:32.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [8 1]}}
    {:id #uuid "bbffb793-a889-40b6-8a4d-f87f40880118",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:32.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [12 1]}}
    {:id #uuid "39c741c1-cea0-49ce-ba54-b5922b6b584d",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:32.553-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :direction :west}}
    {:id #uuid "8d6b08ce-9d15-45bf-ae7e-035b4621a205",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:32.553-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [14 5],
      :direction :west}}
    {:id #uuid "92cc8eef-abcb-4f5b-b068-22a2a6be3418",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:32.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 4],
      :direction :south}}
    {:id #uuid "855d23d8-6430-4af1-8f5e-881b2a88de44",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:32.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 5],
      :direction :south}}
    {:id #uuid "194dbc2e-048a-457a-89fa-ee12da916c66",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:32.156-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :direction :south}}
    {:id #uuid "f75f803d-2fc3-4887-b5d8-45b84f86bd99",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:32.156-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 4],
      :direction :south}}
    {:id #uuid "33221d2b-9a9a-436b-851d-c021d77e6b24",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:31.954-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 3],
      :fire-length 3,
      :bomb-added-timestamp #inst "2021-09-15T17:08:31.942-00:00"}}
    {:id #uuid "a0843d73-608d-4a39-86d7-35b0fea12148",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:31.954-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :fire-length 3,
      :timestamp #inst "2021-09-15T17:08:31.942-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "314724ac-476e-416c-911a-0f0d742b441f",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:31.552-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 3],
      :direction :west}}
    {:id #uuid "80b6fdb9-9141-4ddb-b77c-ce4d8b66f93c",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:31.551-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 3],
      :direction :west}}
    {:id #uuid "63cffe88-e8d3-4f8c-a3e0-151e723f9774",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 3],
      :direction :west}}
    {:id #uuid "9935c7e4-62fb-45a6-9497-8fd0dfce415c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [11 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:31.342-00:00"}}
    {:id #uuid "0afef524-f956-482f-b777-36c66795a949",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [10 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:31.342-00:00"}}
    {:id #uuid "9d51300a-4747-43a0-bc12-9b4980cde762",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [9 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:31.342-00:00"}}
    {:id #uuid "ef7d4d7e-668e-432e-82b6-97c105a779c6",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [8 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:31.342-00:00"}}
    {:id #uuid "4ed1fe0d-ff3d-4217-a8ea-7e7b503fb680",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [12 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:31.342-00:00"}}
    {:id #uuid "7056780c-6f79-40f9-b091-1b7bccc276e4",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [11 2],
      :fire-start-timestamp #inst "2021-09-15T17:08:31.342-00:00"}}
    {:id #uuid "e6ecb37c-1f9c-45c3-8c2f-9f7365d62fac",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [8 1]}}
    {:id #uuid "f659ad0e-cee5-477a-b327-9996294a708e",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [11 2]}}
    {:id #uuid "93b2f7d4-224b-4191-bdc1-fbe0957c2582",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [12 1]}}
    {:id #uuid "99a1dc8a-f18e-4ae5-b560-f5179724b954",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [11 1],
      :fire-length 3}}
    {:id #uuid "c9daf42f-24d9-4416-b23f-ab7428dadf17",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 3],
      :direction :west}}
    {:id #uuid "d50dcf0f-9a09-4e92-95ba-b5a788d6f5b0",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:31.345-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [11 1]}}
    {:id #uuid "efc74311-b02f-4149-90bf-8f32b4cdeb11",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:31.149-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 2],
      :direction :south}}
    {:id #uuid "cf163dee-3cae-4fcb-9194-c463d8634703",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:31.149-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [17 3],
      :direction :south}}
    {:id #uuid "30b52c53-09c6-471e-a4f0-199d855db6c9",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:30.950-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 1],
      :direction :south}}
    {:id #uuid "c71c598d-34c5-4964-bdfc-7dbe8c7fe89c",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:30.950-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [17 2],
      :direction :south}}
    {:id #uuid "a8e4f99e-02a4-4dd4-a667-760afc2ac746",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:30.751-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 1],
      :direction :east}}
    {:id #uuid "1f9f5e96-8eae-46df-b955-2e3a71d55e9a",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:30.751-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [17 1],
      :direction :east}}
    {:id #uuid "678e7f4f-6116-495c-83de-ef58ad8f4679",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-throw-bomb",
     :time #inst "2021-09-15T17:08:29.361-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :players-current-xy [16 1],
      :player-facing-direction :west}}
    {:id #uuid "ce621e68-8134-4573-a903-9fb036f94a4f",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:29.361-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [11 1],
      :fire-length 3,
      :bomb-added-timestamp #inst "2021-09-15T17:08:28.142-00:00"}}
    {:id #uuid "cc5c8046-0d5e-4847-a09e-0a8e448536c5",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:29.361-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 1]}}
    {:id #uuid "ce7acf64-e732-4438-99ca-6993feb5edbb",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:29.153-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 1],
      :direction :west}}
    {:id #uuid "42172116-9ec3-4fbb-a382-99ec8f28f50b",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:28.350-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :direction :east}}
    {:id #uuid "8031ca32-c26a-4205-aaa7-03a95967883f",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:28.350-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 1],
      :direction :east}}
    {:id #uuid "56972635-07e2-4cb1-81ef-1fbabbdf5fdb",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:28.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 1],
      :fire-length 3,
      :bomb-added-timestamp #inst "2021-09-15T17:08:28.142-00:00"}}
    {:id #uuid "61187348-6fc2-4852-b2f7-40e8be86c9f8",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:28.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :fire-length 3,
      :timestamp #inst "2021-09-15T17:08:28.142-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "11cd5e77-8cf2-405d-9051-5b1d1fcb10b3",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:27.750-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 1],
      :direction :west}}
    {:id #uuid "0fc122e7-ee1a-4ec8-888f-7a36eb4fcf05",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:27.750-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 1],
      :direction :west}}
    {:id #uuid "1c247e44-637e-48b4-b4b6-933884600f7a",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:27.150-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 1],
      :direction :east}}
    {:id #uuid "d7137f86-b932-47f9-9a14-bda103c5b421",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:27.149-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :direction :east}}
    {:id #uuid "8affa7d3-d547-4d22-83cb-8f0c83b65e2c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:26.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [10 1]}}
    {:id #uuid "88343b3b-7aba-426e-b0b9-b7c6ceb4415a",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:26.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [9 1]}}
    {:id #uuid "5dbecbaf-45e5-4718-9d57-c3fc150917ba",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:26.957-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [11 1]}}
    {:id #uuid "6f52c7ff-600e-4afe-bc81-c36ad0380dba",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:26.352-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :direction :west}}
    {:id #uuid "274d0880-6921-45f6-8132-75a7db11e319",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:26.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 1],
      :direction :west}}
    {:id #uuid "e4d5f79b-94a6-4883-b5c2-36520225052f",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:26.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 1],
      :direction :west}}
    {:id #uuid "367f63ed-78a4-46ad-a83f-a2fff21835a0",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:25.358-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [9 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:25.342-00:00"}}
    {:id #uuid "83a20a9d-84e7-41f3-98df-9b13cf95d5f7",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:25.358-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [11 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:25.342-00:00"}}
    {:id #uuid "b6b727fc-ee1b-4770-aeae-8d0cbe26d416",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:25.358-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [9 1]}}
    {:id #uuid "9528422d-b703-4c7c-a0c0-c64206d9f734",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:25.358-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [11 1]}}
    {:id #uuid "e341aba2-3e2b-4018-841b-e1f22b812c88",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:25.358-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [10 1],
      :fire-length 3}}
    {:id #uuid "09f29f50-eca8-4b6a-8df5-810ab21d70dc",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:25.358-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [10 1]}}
    {:id #uuid "a026872d-c2dd-4581-b295-eebfb0d7ba08",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:25.357-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [10 1],
      :fire-start-timestamp #inst "2021-09-15T17:08:25.342-00:00"}}
    {:id #uuid "962aa9b6-3cb0-4118-9098-d29eed239c63",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:23.585-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [10 1],
      :fire-length 3,
      :bomb-added-timestamp #inst "2021-09-15T17:08:22.143-00:00"}}
    {:id #uuid "41a8a064-a670-4719-984f-73787b30d72c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:23.585-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 1]}}
    {:id #uuid "cc323da4-db30-413f-a20a-8a190054b395",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-throw-bomb",
     :time #inst "2021-09-15T17:08:23.581-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :players-current-xy [16 1],
      :player-facing-direction :west}}
    {:id #uuid "b36735d1-8f51-4a26-9e92-42a610157cd7",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:23.350-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 1],
      :direction :west}}
    {:id #uuid "26cfefd7-d74d-41fa-ba1a-234c426984c4",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:22.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :direction :east}}
    {:id #uuid "d51c03ae-2417-494c-914d-e76984194133",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:22.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 1],
      :direction :east}}
    {:id #uuid "bc7990b6-fac0-40a8-8b0b-04605d60e344",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:22.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 1],
      :fire-length 3,
      :bomb-added-timestamp #inst "2021-09-15T17:08:22.143-00:00"}}
    {:id #uuid "4236eaf4-6e11-4502-bb81-62242aa343e1",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:22.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 1],
      :fire-length 3,
      :timestamp #inst "2021-09-15T17:08:22.143-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "5ca66320-d883-45ca-b250-6aed9e2e8318",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:21.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 1],
      :direction :west}}
    {:id #uuid "dd1c1480-8fef-454e-9c0a-0f82ddcfa4eb",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:21.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 1],
      :direction :west}}
    {:id #uuid "73cf0248-8277-4fbd-96b4-4b355cc00034",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:20.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 1],
      :direction :west}}
    {:id #uuid "3cb2b3e7-3dc4-4952-acde-ea53fdea1b64",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:20.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 1],
      :direction :west}}
    {:id #uuid "5db79073-c165-47e3-948c-923950c205fd",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:19.950-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 2],
      :direction :north}}
    {:id #uuid "9c86ba0a-5f2e-4249-8244-f2c3afe95f39",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:19.950-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [17 1],
      :direction :north}}
    {:id #uuid "1996bee2-a59d-4901-94b8-ed597262d5c6",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:19.349-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 2],
      :direction :west}}
    {:id #uuid "a0d65c62-b596-4fb3-a35f-132a8b4a33cf",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:18.955-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 3],
      :direction :north}}
    {:id #uuid "97399d5a-802a-42ce-b3fb-1939ad5be503",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:18.955-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [17 2],
      :direction :north}}
    {:id #uuid "481b1466-8720-417c-8c66-10894aed6ae7",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:18.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 3],
      :direction :east}}
    {:id #uuid "5de2b715-d7aa-4cc4-a5e1-b53403971bb0",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:18.550-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [17 3],
      :direction :east}}
    {:id #uuid "a3ad359e-3ad4-4926-8616-6e6a8ac1295e",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:18.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :direction :east}}
    {:id #uuid "d1c5febe-632f-4e6d-a1fd-3c9d5cdae580",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:18.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 3],
      :direction :east}}
    {:id #uuid "a630a8ae-ffb8-4c1b-8adf-58f6d67ce0ca",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:18.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 3],
      :direction :north}}
    {:id #uuid "81c3308b-a0ba-4469-a9bf-bf46e945884e",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "picks-fire-inc-item",
     :time #inst "2021-09-15T17:08:17.751-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :item-position-xy [15 3],
      :new-fire-length 3}}
    {:id #uuid "e23e2832-d2e3-4b18-bee5-a4125b26ba91",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:17.551-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 4],
      :direction :north}}
    {:id #uuid "cd5d6733-71ee-4d31-83f5-26848dc61250",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:17.551-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 3],
      :direction :north}}
    {:id #uuid "18eb1545-9670-4ce4-a157-0b936d921b8b",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:17.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 4],
      :direction :north}}
    {:id #uuid "3ef2af6a-4043-4b60-b72e-02bc4fb8a0cf",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:17.350-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :direction :north}}
    {:id #uuid "7af87443-c210-4fb8-99ee-62a14c744817",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:17.150-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 6],
      :direction :north}}
    {:id #uuid "7c00235d-d027-4238-868b-6420df0dc769",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:17.150-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 5],
      :direction :north}}
    {:id #uuid "b7eaa418-9e7f-42a8-85c7-8af80105b7c0",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:16.949-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 7],
      :direction :north}}
    {:id #uuid "aec96bbb-33ac-45ad-bc97-cb2c27169fd6",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:16.949-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 6],
      :direction :north}}
    {:id #uuid "95c50af1-d9e9-44eb-bf44-b5ca8eaf5cc0",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:16.749-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 8],
      :direction :north}}
    {:id #uuid "1fc4e9f2-595d-47ad-ad49-11982d9d73c5",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:16.749-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 7],
      :direction :north}}
    {:id #uuid "f6a62892-cf50-4e10-b306-8a6a3220669c",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:16.551-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 9],
      :direction :north}}
    {:id #uuid "845b02ec-9e53-43a0-bb8e-965b4d39bd13",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:16.551-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 8],
      :direction :north}}
    {:id #uuid "7fdecf0c-2153-4f61-a981-d97aea320f5a",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:16.149-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 9],
      :direction :west}}
    {:id #uuid "3681e88a-6136-45d6-9d03-9b2f750f5ce3",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:16.149-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 9],
      :direction :west}}
    {:id #uuid "16b16196-d096-4528-bc36-1aa66d2922b2",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 6]}}
    {:id #uuid "65bbfb06-9ff3-4e09-9692-658a4094f0f6",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [14 5]}}
    {:id #uuid "d2609fc8-1ed2-4cd0-a2c8-9329b02c2ec6",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [16 5]}}
    {:id #uuid "6fb163d3-3461-4fd2-b3ee-a479e45c94e7",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 7]}}
    {:id #uuid "ba635360-9736-4fbc-8c73-a7befc7508c9",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 5]}}
    {:id #uuid "eed3d7c6-97cc-4100-a263-8f1a36213470",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 4]}}
    {:id #uuid "b85a4516-c7dd-49a3-a489-3be5cc054168",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 9]}}
    {:id #uuid "966d28bc-7d8b-448c-9d86-7315fae68122",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 8]}}
    {:id #uuid "569e95cd-5e1f-420b-8dd5-ff84ebb25cd0",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [17 7]}}
    {:id #uuid "016f5c64-ce8f-458f-91cc-3f69db5a43ae",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [16 7]}}
    {:id #uuid "d47cec94-d9ab-4b85-8128-475ae9370658",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [13 7]}}
    {:id #uuid "90b316ac-fb56-495c-9979-8939ff2f667b",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [14 7]}}
    {:id #uuid "9fa3dc18-2f15-4ba2-a838-b23f195f6a7d",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:15.592-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 3]}}
    {:id #uuid "651d7876-028c-4622-b59f-f064861284f0",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [14 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "b608b7a7-f46c-488c-8052-2c4b2bd3d16b",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [16 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "6b98c385-d952-4592-90fa-2bb80b88c456",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 6],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "ed271486-d854-4398-a99a-1a4badc03fda",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "abb263ad-304c-444b-a0ab-07818bf57e83",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [17 7]}}
    {:id #uuid "fa328a4d-7df5-444b-8ad8-17048587c993",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [14 5]}}
    {:id #uuid "831ede30-77bc-4a50-9f38-f72e4a4e47b0",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [16 5]}}
    {:id #uuid "2c0dd027-8641-456a-8c20-d0ece88e3d4b",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [15 3]}}
    {:id #uuid "3cf677d6-7dde-4d39-8f38-602af753f83a",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [15 7],
      :fire-length 2}}
    {:id #uuid "0c094e10-2864-4c0b-830d-a6fcfa38639c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [15 5],
      :fire-length 2}}
    {:id #uuid "66152e49-c4bf-4fbc-856d-ecbea82bdf17",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 7]}}
    {:id #uuid "57edff70-2fbc-47ae-b34c-b41cb5ba64a6",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:13.971-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 5]}}
    {:id #uuid "ad62d73b-3380-4d31-b534-e5a182285866",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.970-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "3dc71142-3f4f-4bb7-b901-3337558b6442",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.970-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "ea6ba55c-6202-4bf5-a90f-2eecc944e07b",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.970-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 4],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "78e3250a-9cc7-46d2-acca-d37fff528c24",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.970-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 3],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "a3159c8a-1770-462e-acc8-c66f0c805b52",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 9],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "faedf4ed-5912-4811-ac79-6be457fc9445",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 8],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "2ef91308-412c-4a7e-b88d-73e6b455a35e",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [17 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "0d167b98-ab6f-4c4d-9535-1cdda6a4021e",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [16 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "f3e3fe3a-afe0-4b01-8b3f-edcbc1b52f54",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [13 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "db9fc8c8-e041-49f2-b4be-bc1f79b3870c",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [14 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "c5b61f74-9550-4b65-9d5b-d813803acbe0",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 5],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "5f6bc66f-d2e7-4ce6-a0e3-5fb9117d9a91",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:13.969-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 6],
      :fire-start-timestamp #inst "2021-09-15T17:08:13.942-00:00"}}
    {:id #uuid "94460689-9e53-491d-9345-aba45f9daf5a",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:12.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 9],
      :direction :east}}
    {:id #uuid "5e9fae9d-1d43-4ad3-ba5d-ff1c1e30bb37",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:12.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 9],
      :direction :east}}
    {:id #uuid "092664e4-21a2-415f-83b6-d23235c8071a",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:11.953-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 9],
      :direction :south}}
    {:id #uuid "f886c875-966f-4602-8ee7-b65e7c0ff455",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:11.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 8],
      :direction :south}}
    {:id #uuid "71be3ab1-37d8-4b68-bc1f-dad99efad499",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:11.752-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 7],
      :direction :south}}
    {:id #uuid "73658177-9649-4bc1-b0c4-192e26759c1d",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:11.752-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 8],
      :direction :south}}
    {:id #uuid "d76ba63a-b6eb-4f59-aa8e-cca9df7c8748",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:11.555-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 7],
      :fire-length 2,
      :bomb-added-timestamp #inst "2021-09-15T17:08:11.543-00:00"}}
    {:id #uuid "23f4a027-1744-4f17-8745-65789cded917",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:11.555-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 7],
      :fire-length 2,
      :timestamp #inst "2021-09-15T17:08:11.543-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "c1c97fb2-5909-4da6-8b43-77c6dd2233b1",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:11.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 7],
      :direction :south}}
    {:id #uuid "d60ad24a-ae2e-4e31-992e-c0a87d48e679",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:11.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 6],
      :direction :south}}
    {:id #uuid "d930a718-e7ca-4c3f-a693-053d800d15a8",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:10.953-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :direction :south}}
    {:id #uuid "1f5aeb1e-b0f7-4ae5-b278-0c5e0cde5ce4",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:10.953-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 6],
      :direction :south}}
    {:id #uuid "4002a541-9230-4e19-bd0e-a87399c8fdda",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:10.752-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 5],
      :fire-length 2,
      :bomb-added-timestamp #inst "2021-09-15T17:08:10.743-00:00"}}
    {:id #uuid "8610e059-97e7-426e-8ade-2010720357ee",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:10.752-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :fire-length 2,
      :timestamp #inst "2021-09-15T17:08:10.743-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "d9969072-feae-4a1b-8857-2770bd185a6d",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:10.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 4],
      :direction :south}}
    {:id #uuid "3da88e0b-8c20-4883-8981-2f92331d3ff2",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:10.151-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 5],
      :direction :south}}
    {:id #uuid "99652696-f50d-433b-8714-5f74ab9b6d58",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:09.751-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 5],
      :direction :north}}
    {:id #uuid "50a087bd-38f2-4aab-b432-c84b501e66a7",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:09.751-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 4],
      :direction :north}}
    {:id #uuid "9d035884-2e30-419c-a7c9-712d9d5ee4d1",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:09.352-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 5],
      :direction :north}}
    {:id #uuid "9ca5eee9-e01a-487f-a6d2-19c06d1652d9",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:09.351-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 6],
      :direction :north}}
    {:id #uuid "eee21189-4daa-4838-8952-26dd338e515c",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:09.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 7],
      :direction :north}}
    {:id #uuid "5cdec876-3a8b-435f-b6e2-80edb619b512",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:09.152-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 6],
      :direction :north}}
    {:id #uuid "806e8ac1-b926-45fc-a453-1611466c7731",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:08.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 7],
      :direction :north}}
    {:id #uuid "a4ac04ca-1423-4324-b3d3-ff35e68663d5",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:08.951-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 8],
      :direction :north}}
    {:id #uuid "b29788c8-b273-40e7-b38e-7c9dcdef9292",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:08.752-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 9],
      :direction :north}}
    {:id #uuid "4d77c532-ed82-4709-ba0d-21cf2cb4d596",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:08.752-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 8],
      :direction :north}}
    {:id #uuid "32c08b9b-84cf-432b-8eb2-818d52e5edf5",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:08.552-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 9],
      :direction :west}}
    {:id #uuid "4902db31-8121-4675-b0b9-ec520bcb6838",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:08.552-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 9],
      :direction :west}}
    {:id #uuid "b056b736-ed3b-4d1a-b070-aef2410ad00f",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:07.983-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 6]}}
    {:id #uuid "b6de8508-c5bc-4292-9bca-4b1c3b906161",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:07.983-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [14 7]}}
    {:id #uuid "81032d95-8102-4151-9448-59aeaa70f548",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:07.983-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [13 7]}}
    {:id #uuid "ad690043-b639-4cf5-9c40-013e9b5d69d3",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:07.983-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [16 7]}}
    {:id #uuid "506f2075-86ac-4d4f-9fc6-6d222c9f466a",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:07.983-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 8]}}
    {:id #uuid "e9185cb1-2e54-4e46-ba6d-192b7bd8bf7d",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:07.983-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 9]}}
    {:id #uuid "9a67e516-35f3-48d3-bd43-18f878140d23",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-remove",
     :time #inst "2021-09-15T17:08:07.980-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire-position-xy [15 7]}}
    {:id #uuid "b95b1b32-0b25-401f-9d48-a9af520b1ce5",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-remove",
     :time #inst "2021-09-15T17:08:06.395-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :bomb-position-xy [15 7]}}
    {:id #uuid "c6dc6182-e550-450d-93a3-9c51565aecd7",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-exploading",
     :time #inst "2021-09-15T17:08:06.393-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :position-xy [15 7],
      :fire-length 2}}
    {:id #uuid "6c828672-dbab-46a3-bff8-58b4bde16557",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:06.392-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [16 7]}}
    {:id #uuid "0a33dab7-b480-4375-8533-812e79e1cb89",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:06.392-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [13 7]}}
    {:id #uuid "b64d55cf-7a81-48bf-8c61-9e8c3e41fcbf",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:06.389-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 6],
      :fire-start-timestamp #inst "2021-09-15T17:08:06.343-00:00"}}
    {:id #uuid "820fd5a9-5c97-4728-8b67-06abd4ad2585",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:06.389-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [14 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:06.343-00:00"}}
    {:id #uuid "d5012281-d50b-4a35-9085-01791bf7cb72",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:06.389-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [13 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:06.343-00:00"}}
    {:id #uuid "1cd216d8-d676-4f0c-b15d-c0d9a6d2dadd",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:06.389-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [16 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:06.343-00:00"}}
    {:id #uuid "2bfc7640-43d5-47ec-8845-92eb152ff96a",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:06.389-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 8],
      :fire-start-timestamp #inst "2021-09-15T17:08:06.343-00:00"}}
    {:id #uuid "8317d9fb-8623-4604-a44a-d60efc49422d",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:06.389-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 9],
      :fire-start-timestamp #inst "2021-09-15T17:08:06.343-00:00"}}
    {:id #uuid "f6ab3145-f7cc-4b43-82f9-e269c5b45d9b",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "stone-to-remove",
     :time #inst "2021-09-15T17:08:06.389-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747", :position-xy [15 6]}}
    {:id #uuid "db2f4bcc-bd5a-4a41-9b6e-507d33ee89ef",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "fire-to-add",
     :time #inst "2021-09-15T17:08:06.385-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :fire-position-xy [15 7],
      :fire-start-timestamp #inst "2021-09-15T17:08:06.343-00:00"}}
    {:id #uuid "0dbf9392-4cee-4775-8594-f9d2e8b8851d",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:03.555-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 9],
      :direction :east}}
    {:id #uuid "bb3602d7-0278-49de-88b8-04c22674b1ef",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:03.555-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 9],
      :direction :east}}
    {:id #uuid "b4b815cb-1ae4-4764-bae7-bd60128c580b",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:03.355-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 8],
      :direction :south}}
    {:id #uuid "c154c2be-f1ec-4b74-af21-616786b4ca0c",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:03.355-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 9],
      :direction :south}}
    {:id #uuid "6c57a825-74eb-4042-9753-3491bf6e857e",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:03.178-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 8],
      :direction :south}}
    {:id #uuid "17e20172-1d5d-42ab-9abd-2de8507e5956",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-place-bomb",
     :time #inst "2021-09-15T17:08:03.174-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 7],
      :fire-length 2,
      :timestamp #inst "2021-09-15T17:08:03.144-00:00",
      :max-nr-of-bombs-for-player 3}}
    {:id #uuid "d5dd5dc6-255d-47aa-b479-3e5b0718bbde",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:03.169-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 7],
      :direction :south}}
    {:id #uuid "f64a384b-e0b2-49e4-a895-3930215a002f",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "bomb-to-add",
     :time #inst "2021-09-15T17:08:03.169-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :bomb-position-xy [15 7],
      :fire-length 2,
      :bomb-added-timestamp #inst "2021-09-15T17:08:03.144-00:00"}}
    {:id #uuid "680467b6-e18b-44ed-ba06-5b3879502617",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:02.554-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 8],
      :direction :north}}
    {:id #uuid "a81b3df1-c5a7-4e75-9dd8-aa829a38a3f4",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:02.554-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 7],
      :direction :north}}
    {:id #uuid "9986a504-0e99-4a6b-ab10-5071134513a0",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:02.355-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 9],
      :direction :north}}
    {:id #uuid "894f35a3-1291-4ce4-a83c-0b478a6579e8",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:02.355-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 8],
      :direction :north}}
    {:id #uuid "851a859f-3b03-4969-8fb7-3023545c0e60",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:02.155-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [15 9],
      :direction :west}}
    {:id #uuid "68b9b217-e5f9-44cb-932f-80fc79573674",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:01.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [16 9],
      :direction :west}}
    {:id #uuid "b8e41129-815a-4545-bbf4-11ccd0409b8f",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:01.952-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [15 9],
      :direction :west}}
    {:id #uuid "2878ce06-2099-408a-b2b4-622fb350388e",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "move",
     :time #inst "2021-09-15T17:08:01.775-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :next-position [16 9],
      :direction :west}}
    {:id #uuid "9b376184-bba0-4ddf-9cd9-588b156a1002",
     :source "urn:se:jherrlin:bomberman:player",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "wants-to-move",
     :time #inst "2021-09-15T17:08:01.769-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :current-xy [17 9],
      :direction :west}}
    {:id #uuid "1f4cdf6e-ba09-4c92-b226-ecc2b673b1c1",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "start",
     :time #inst "2021-09-15T17:08:00.306-00:00",
     :content-type "application/edn"}
    {:id #uuid "c76ed853-aeb5-40f5-9d52-04c322fd6d25",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "join-game",
     :time #inst "2021-09-15T17:07:58.808-00:00",
     :content-type "application/edn",
     :data
     {:user-facing-direction :south,
      :max-nr-of-bombs-for-user 3,
      :position [1 1],
      :fire-length 2,
      :game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "b4581a30-6bd4-46ea-b0c1-a5ae8e3418ff",
      :player-name "Hannah"}}
    {:id #uuid "88bae521-4443-4012-9f22-d2f553c039f5",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "join-game",
     :time #inst "2021-09-15T17:07:47.309-00:00",
     :content-type "application/edn",
     :data
     {:user-facing-direction :south,
      :max-nr-of-bombs-for-user 3,
      :position [1 1],
      :fire-length 2,
      :game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :player-id #uuid "bb6c0540-1cec-48d7-9b08-15427be67011",
      :player-name "John"}}
    {:id #uuid "dba4f2b8-00e8-4d8a-b04c-bb024cc6d6cf",
     :source "urn:se:jherrlin:bomberman:game",
     :subject #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
     :type "create-game",
     :time #inst "2021-09-15T17:07:39.854-00:00",
     :content-type "application/edn",
     :data
     {:game-id #uuid "c1b5bc52-a5e0-4f48-ac4d-da76bbe1d747",
      :fire (),
      :stones
      ([4 1]
       [7 1]
       [8 1]
       [9 1]
       [11 1]
       [12 1]
       [13 1]
       [14 1]
       [3 2]
       [5 2]
       [7 2]
       [9 2]
       [11 2]
       [13 2]
       [15 2]
       [2 3]
       [3 3]
       [4 3]
       [5 3]
       [7 3]
       [8 3]
       [10 3]
       [14 3]
       [15 3]
       [1 4]
       [3 4]
       [5 4]
       [7 4]
       [11 4]
       [17 4]
       [1 5]
       [2 5]
       [3 5]
       [4 5]
       [5 5]
       [6 5]
       [8 5]
       [9 5]
       [11 5]
       [12 5]
       [13 5]
       [14 5]
       [16 5]
       [17 5]
       [1 6]
       [3 6]
       [11 6]
       [13 6]
       [15 6]
       [17 6]
       [1 7]
       [2 7]
       [3 7]
       [6 7]
       [7 7]
       [10 7]
       [13 7]
       [16 7]
       [17 7]
       [11 8]
       [3 9]
       [4 9]
       [5 9]
       [7 9]
       [8 9]
       [10 9]
       [11 9]
       [12 9]
       [13 9]
       [14 9]),
      :password "pwd",
      :game-name "JohnsGame",
      :game-state :created,
      :flying-bombs (),
      :game-password "pwd",
      :items
      ({:item-position-xy [5 2], :item-power :inc-fire-length}
       {:item-position-xy [7 2], :item-power :inc-fire-length}
       {:item-position-xy [9 2], :item-power :inc-fire-length}
       {:item-position-xy [4 3], :item-power :inc-fire-length}
       {:item-position-xy [7 3], :item-power :inc-fire-length}
       {:item-position-xy [8 3], :item-power :inc-fire-length}
       {:item-position-xy [14 3], :item-power :inc-fire-length}
       {:item-position-xy [15 3], :item-power :inc-fire-length}
       {:item-position-xy [5 4], :item-power :inc-fire-length}
       {:item-position-xy [8 5], :item-power :inc-fire-length}
       {:item-position-xy [11 5], :item-power :inc-fire-length}
       {:item-position-xy [3 7], :item-power :inc-fire-length}
       {:item-position-xy [6 7], :item-power :inc-fire-length}
       {:item-position-xy [12 9], :item-power :inc-fire-length}),
      :bombs (),
      :board
      [[{:type :wall, :x 0, :y 0}
        {:type :wall, :x 1, :y 0}
        {:type :wall, :x 2, :y 0}
        {:type :wall, :x 3, :y 0}
        {:type :wall, :x 4, :y 0}
        {:type :wall, :x 5, :y 0}
        {:type :wall, :x 6, :y 0}
        {:type :wall, :x 7, :y 0}
        {:type :wall, :x 8, :y 0}
        {:type :wall, :x 9, :y 0}
        {:type :wall, :x 10, :y 0}
        {:type :wall, :x 11, :y 0}
        {:type :wall, :x 12, :y 0}
        {:type :wall, :x 13, :y 0}
        {:type :wall, :x 14, :y 0}
        {:type :wall, :x 15, :y 0}
        {:type :wall, :x 16, :y 0}
        {:type :wall, :x 17, :y 0}
        {:type :wall, :x 18, :y 0}]
       [{:type :wall, :x 0, :y 1}
        {:type :floor, :x 1, :y 1}
        {:type :floor, :x 2, :y 1}
        {:type :floor, :x 3, :y 1}
        {:type :floor, :x 4, :y 1}
        {:type :floor, :x 5, :y 1}
        {:type :floor, :x 6, :y 1}
        {:type :floor, :x 7, :y 1}
        {:type :floor, :x 8, :y 1}
        {:type :floor, :x 9, :y 1}
        {:type :floor, :x 10, :y 1}
        {:type :floor, :x 11, :y 1}
        {:type :floor, :x 12, :y 1}
        {:type :floor, :x 13, :y 1}
        {:type :floor, :x 14, :y 1}
        {:type :floor, :x 15, :y 1}
        {:type :floor, :x 16, :y 1}
        {:type :floor, :x 17, :y 1}
        {:type :wall, :x 18, :y 1}]
       [{:type :wall, :x 0, :y 2}
        {:type :floor, :x 1, :y 2}
        {:type :wall, :x 2, :y 2}
        {:type :floor, :x 3, :y 2}
        {:type :wall, :x 4, :y 2}
        {:type :floor, :x 5, :y 2}
        {:type :wall, :x 6, :y 2}
        {:type :floor, :x 7, :y 2}
        {:type :wall, :x 8, :y 2}
        {:type :floor, :x 9, :y 2}
        {:type :wall, :x 10, :y 2}
        {:type :floor, :x 11, :y 2}
        {:type :wall, :x 12, :y 2}
        {:type :floor, :x 13, :y 2}
        {:type :wall, :x 14, :y 2}
        {:type :floor, :x 15, :y 2}
        {:type :wall, :x 16, :y 2}
        {:type :floor, :x 17, :y 2}
        {:type :wall, :x 18, :y 2}]
       [{:type :wall, :x 0, :y 3}
        {:type :floor, :x 1, :y 3}
        {:type :floor, :x 2, :y 3}
        {:type :floor, :x 3, :y 3}
        {:type :floor, :x 4, :y 3}
        {:type :floor, :x 5, :y 3}
        {:type :floor, :x 6, :y 3}
        {:type :floor, :x 7, :y 3}
        {:type :floor, :x 8, :y 3}
        {:type :floor, :x 9, :y 3}
        {:type :floor, :x 10, :y 3}
        {:type :floor, :x 11, :y 3}
        {:type :floor, :x 12, :y 3}
        {:type :floor, :x 13, :y 3}
        {:type :floor, :x 14, :y 3}
        {:type :floor, :x 15, :y 3}
        {:type :floor, :x 16, :y 3}
        {:type :floor, :x 17, :y 3}
        {:type :wall, :x 18, :y 3}]
       [{:type :wall, :x 0, :y 4}
        {:type :floor, :x 1, :y 4}
        {:type :wall, :x 2, :y 4}
        {:type :floor, :x 3, :y 4}
        {:type :wall, :x 4, :y 4}
        {:type :floor, :x 5, :y 4}
        {:type :wall, :x 6, :y 4}
        {:type :floor, :x 7, :y 4}
        {:type :wall, :x 8, :y 4}
        {:type :floor, :x 9, :y 4}
        {:type :wall, :x 10, :y 4}
        {:type :floor, :x 11, :y 4}
        {:type :wall, :x 12, :y 4}
        {:type :floor, :x 13, :y 4}
        {:type :wall, :x 14, :y 4}
        {:type :floor, :x 15, :y 4}
        {:type :wall, :x 16, :y 4}
        {:type :floor, :x 17, :y 4}
        {:type :wall, :x 18, :y 4}]
       [{:type :wall, :x 0, :y 5}
        {:type :floor, :x 1, :y 5}
        {:type :floor, :x 2, :y 5}
        {:type :floor, :x 3, :y 5}
        {:type :floor, :x 4, :y 5}
        {:type :floor, :x 5, :y 5}
        {:type :floor, :x 6, :y 5}
        {:type :floor, :x 7, :y 5}
        {:type :floor, :x 8, :y 5}
        {:type :floor, :x 9, :y 5}
        {:type :floor, :x 10, :y 5}
        {:type :floor, :x 11, :y 5}
        {:type :floor, :x 12, :y 5}
        {:type :floor, :x 13, :y 5}
        {:type :floor, :x 14, :y 5}
        {:type :floor, :x 15, :y 5}
        {:type :floor, :x 16, :y 5}
        {:type :floor, :x 17, :y 5}
        {:type :wall, :x 18, :y 5}]
       [{:type :wall, :x 0, :y 6}
        {:type :floor, :x 1, :y 6}
        {:type :wall, :x 2, :y 6}
        {:type :floor, :x 3, :y 6}
        {:type :wall, :x 4, :y 6}
        {:type :floor, :x 5, :y 6}
        {:type :wall, :x 6, :y 6}
        {:type :floor, :x 7, :y 6}
        {:type :wall, :x 8, :y 6}
        {:type :floor, :x 9, :y 6}
        {:type :wall, :x 10, :y 6}
        {:type :floor, :x 11, :y 6}
        {:type :wall, :x 12, :y 6}
        {:type :floor, :x 13, :y 6}
        {:type :wall, :x 14, :y 6}
        {:type :floor, :x 15, :y 6}
        {:type :wall, :x 16, :y 6}
        {:type :floor, :x 17, :y 6}
        {:type :wall, :x 18, :y 6}]
       [{:type :wall, :x 0, :y 7}
        {:type :floor, :x 1, :y 7}
        {:type :floor, :x 2, :y 7}
        {:type :floor, :x 3, :y 7}
        {:type :floor, :x 4, :y 7}
        {:type :floor, :x 5, :y 7}
        {:type :floor, :x 6, :y 7}
        {:type :floor, :x 7, :y 7}
        {:type :floor, :x 8, :y 7}
        {:type :floor, :x 9, :y 7}
        {:type :floor, :x 10, :y 7}
        {:type :floor, :x 11, :y 7}
        {:type :floor, :x 12, :y 7}
        {:type :floor, :x 13, :y 7}
        {:type :floor, :x 14, :y 7}
        {:type :floor, :x 15, :y 7}
        {:type :floor, :x 16, :y 7}
        {:type :floor, :x 17, :y 7}
        {:type :wall, :x 18, :y 7}]
       [{:type :wall, :x 0, :y 8}
        {:type :floor, :x 1, :y 8}
        {:type :wall, :x 2, :y 8}
        {:type :floor, :x 3, :y 8}
        {:type :wall, :x 4, :y 8}
        {:type :floor, :x 5, :y 8}
        {:type :wall, :x 6, :y 8}
        {:type :floor, :x 7, :y 8}
        {:type :wall, :x 8, :y 8}
        {:type :floor, :x 9, :y 8}
        {:type :wall, :x 10, :y 8}
        {:type :floor, :x 11, :y 8}
        {:type :wall, :x 12, :y 8}
        {:type :floor, :x 13, :y 8}
        {:type :wall, :x 14, :y 8}
        {:type :floor, :x 15, :y 8}
        {:type :wall, :x 16, :y 8}
        {:type :floor, :x 17, :y 8}
        {:type :wall, :x 18, :y 8}]
       [{:type :wall, :x 0, :y 9}
        {:type :floor, :x 1, :y 9}
        {:type :floor, :x 2, :y 9}
        {:type :floor, :x 3, :y 9}
        {:type :floor, :x 4, :y 9}
        {:type :floor, :x 5, :y 9}
        {:type :floor, :x 6, :y 9}
        {:type :floor, :x 7, :y 9}
        {:type :floor, :x 8, :y 9}
        {:type :floor, :x 9, :y 9}
        {:type :floor, :x 10, :y 9}
        {:type :floor, :x 11, :y 9}
        {:type :floor, :x 12, :y 9}
        {:type :floor, :x 13, :y 9}
        {:type :floor, :x 14, :y 9}
        {:type :floor, :x 15, :y 9}
        {:type :floor, :x 16, :y 9}
        {:type :floor, :x 17, :y 9}
        {:type :wall, :x 18, :y 9}]
       [{:type :wall, :x 0, :y 10}
        {:type :wall, :x 1, :y 10}
        {:type :wall, :x 2, :y 10}
        {:type :wall, :x 3, :y 10}
        {:type :wall, :x 4, :y 10}
        {:type :wall, :x 5, :y 10}
        {:type :wall, :x 6, :y 10}
        {:type :wall, :x 7, :y 10}
        {:type :wall, :x 8, :y 10}
        {:type :wall, :x 9, :y 10}
        {:type :wall, :x 10, :y 10}
        {:type :wall, :x 11, :y 10}
        {:type :wall, :x 12, :y 10}
        {:type :wall, :x 13, :y 10}
        {:type :wall, :x 14, :y 10}
        {:type :wall, :x 15, :y 10}
        {:type :wall, :x 16, :y 10}
        {:type :wall, :x 17, :y 10}
        {:type :wall, :x 18, :y 10}]]}}))
