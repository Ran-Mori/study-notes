# media

## frames

### Intra-coded Frame

* feature
  * It is **self-contained** and does not rely on any other frames for decoding.
  * It contains a full picture, much like a static image (e.g., a JPEG).
* Use Case
  * **Seeking** or jumping to specific points in a video.
  * Synchronization during playback or streaming.

### Predicted Frame

* feature
  * it encodes only the changes relative to the **previous frame** (I-frame or another P-frame).

### Bidirectional Predicted Frame

* it uses **both previous and future frames** for reference to encode changes.

### Group of Pictures

* what
  * it is is a sequence of frames within a video stream, consisting of:
    1. One **I-frame** (anchor).
    2. Several **P-frames** and/or **B-frames**.
* example
  * IBBPBBPBBPBB...

***

## format

| category               | example             |
| ---------------------- | ------------------- |
| container format       | flv/avi/avi/mp4/wmv |
| video container format | h264/avc/vp6/mjpeg  |
| audio container format | mp3/aac             |
| raw video format       | yuv/rgb             |
| raw audio format       | pcm                 |

***

## play process

### decoding protocol

* 通过https/HLS(HTTP Live Streaming)/DASH(Dynamic Adaptive Streaming over HTTP)从server处获取container format流

### demultiplexing

* Extract individual media streams (e.g., video, audio, subtitles) from the container format.
* **Examples**: In an MP4 file, extract H.264 video and AAC audio streams.

### decoding (audio/video)

* **Purpose**: Convert compressed streams into raw data that can be rendered.
* 将h.264, h.265解成yuv/rgb; 将mp3/aac解成pcm.

### synchronizing

* 一般让视频同步音频，如果频繁地去调整音频会产生杂音让人感觉到刺耳不舒服，而人对图像的敏感度就低很多了

### Rendering

* **Video Rendering**: Pass decoded video frames to the display pipeline (e.g., using OpenGL, DirectX, or Vulkan).
* **Audio Playback**: Send decoded audio samples to the audio output system (e.g., speakers or headphones).
