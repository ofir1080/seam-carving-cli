# Seam Carving CLI - java implementation

Seam carving (or liquid rescaling) is an algorithm for content-aware image resizing, developed by Shai Avidan, of Mitsubishi Electric Research Laboratories (MERL), and Ariel Shamir, of the Interdisciplinary Center and MERL. It functions by establishing a number of seams (paths of least importance) in an image and automatically removes seams to reduce image size or inserts seams to extend it. Seam carving also allows manually defining areas in which pixels may not be modified, and features the ability to remove whole objects from photographs ([Wiki](https://en.wikipedia.org/wiki/Seam_carving/)).

### Examples
Original size (1558 x 1138):
![orig_img](https://i.ibb.co/zFqjrNR/Screen-Shot-2020-04-09-at-15-57-17.png)

(900 x 900):
![example2](https://i.ibb.co/Yp6mDtj/resized-image.png)

(1900 x 900):
![example3](https://i.ibb.co/xXxPs2g/resized-image.png)



## Usage
in the appropriate working directory: 
```
>> java -jar seam-carving-cli-all-1.0.jar *image-path*
Image read
new height (original is _):
>> Y
new height (original is _):
>> X
```

large images may take while...

## License
[MIT](https://choosealicense.com/licenses/mit/)
