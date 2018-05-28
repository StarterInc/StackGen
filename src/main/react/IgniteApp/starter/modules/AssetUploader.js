/**
 * Handy AssetUploader functions
 *
 */
var AssetUploader = {

    // upload process is:

    // 0. Load category picker in thread

    // 1. Pick existing or Create New Content file

    // 2. Save content file to S3

    // 3. Display uploaded content file to users
    // - Caption form
    // - Validations

    // 4. Popup category Picker after Caption done

    // 5. Popup Upload Button after category picked

    // 6. Upload metadata when upload button pressed
    // - hide buttons and show loading indicator
    // - show content detail page
    // - popup the 'post upload' actions

    loadCategoriesSpinner : function(dtx, self, callback) {

        var data = [];

        var contentRatingColumn = Ti.UI.createPickerColumn();

        contentRatingColumn.addRow(Ti.UI.createPickerRow({
            title : 'Rated G',
            id : Ti.App.RATING_G,
            height : 10
        }));

        contentRatingColumn.addRow(Ti.UI.createPickerRow({
            title : 'Rated PG',
            id : Ti.App.RATING_PG,
            height : 10
        }));

        contentRatingColumn.addRow(Ti.UI.createPickerRow({
            title : 'Rated R',
            id : Ti.App.RATING_R,
            height : 10
        }));

        contentRatingColumn.addRow(Ti.UI.createPickerRow({
            title : 'N.S.F.W.',
            id : Ti.App.RATING_NSFW,
            height : 10
        }));

        contentRatingColumn.addRow(Ti.UI.createPickerRow({
            title : 'Rated X',
            id : Ti.App.RATING_X,
            height : 10
        }));

        var categoriesColumn = Ti.UI.createPickerColumn();

        for ( t = 0; t < dtx.length; t++) {
            categoriesColumn.addRow(Ti.UI.createPickerRow({
                title : dtx[t].description,
                id : dtx[t].id,
                height : 10
            }));
        }

        var picker = Ti.UI.createPicker({
            //  height : Ti.App.CONTROL_HEIGHT,
            bottom : 0,
            //        type: Ti.UI.PICKER_TYPE_PLAIN,
            useSpinner : true,
            visible : false,
            selectionIndicator : true
        });
        self._picker = picker;
        self.add(self._picker);

        self._picker.setColumns([contentRatingColumn, categoriesColumn]);

        //  height : Ti.App.CONTROL_HEIGHT,)
        var postButton = new Button(null, 'images/icons/icon_upload_dark_128.png');
        postButton.setBottom(150);
        self.postButton = postButton;

        var label = Ti.UI.createLabel({
            color : Ti.App.TITLE_FONT_COLOR,
            text : L('SubmitPost'),
            center : {
                x : '50%',
                y : '60%'
            },

            font : {
                fontSize : 14,
                fontWeight : 'bold',
            },
        });

        postButton.add(label);
        var _this = this;
        this.submitted = false;
        postButton.addEventListener('click', function(e) {
            postButton.hide();
            self._picker.setVisible(false);
            function submitContent(val, self) {

                setTimeout(function() {
                    if ( typeof (self.contentURL) !== 'undefined') {
                        val.createContentRecord(self);
                        this.submitted = true;
                    } else {
                        Ti.API.info(L('uploadInProgress'));
                        self._label.setText(L('uploadInProgress'));
                        submitContent(val, self);
                    }
                }, 1000);

            }

            submitContent(_this, self);

        });
        postButton.setVisible(false);
        self.add(postButton);

        self._picker.addEventListener('change', function(e) {
            if (!this.submitted)
                self.postButton.setVisible(true);

            self.submitCaptionButton.hide();
            // always!
        });

        // now that the spinner is loaded, safe to laod rest of the view
        callback();
        return self._picker;
    },

    uploadFile : function(fileobject, filename, description) {
        new Alert(L('alert'), null, L('todo'));
    },

    uploadMetadata : function(filename, metadata) {
        new Alert(L('alert'), null, L('todo'));
    },

    /**
     * upload content to server
     */
    uploadFromGallery : function(description, self, userimage) {

        var saveLB = Ti.App.prefs.getPreference('upload_location_data_for_posts', '1');

        // no-lbs by default. protect privacy by default

        //TODO: FIX EXIF -- the my Media package broke fatally for iOS7... look for options.  Titanium 3.3.3 is supposed to support native EXIF.
        //
        // custom myMedia module
        // https://github.com/jayshepherd/myMedia-Titanium-Module

        self.latitude = 0.0;
        self.longitude = 0.0;

        var mediatypes = [Ti.Media.MEDIA_TYPE_PHOTO, Ti.Media.MEDIA_TYPE_VIDEO];

        if (Ti.App.connSpeed < 40) {// disallow video uploads for slow network connections
            var mediatypes = [Ti.Media.MEDIA_TYPE_PHOTO];
            new Alert(L('alert'), null, L('NetworkConnectionSlowLimitingToPhotoUploads'));
        }

        var badges = Ti.App.CurrentUser.badges;
        if (badges !== null) {// only allow video if they've earned the badge
            if (badges[0][1] === '0')
                var mediatypes = [Ti.Media.MEDIA_TYPE_PHOTO];
        }
        _this = this;

        var mymedia = require("my.media");
        mymedia.openPhotoGallery({


            allowEditing : !true,

            animated : !true,

            videoMaximumDuration : 60000, // 60 seconds
            videoQuality : Ti.Media.QUALITY_MEDIUM,

            mediaTypes : mediatypes,

            success : function(event) {
                if (userimage === null) {
                    self._label.setVisible(true);
                    self._label.setText(L('uploadInProgressPleaseEnterCaption'));
                } else {
                    self.submitCaptionButton.hide();
                }
                var uie = require('ui/common/ApplicationIndicatorWindow');
                var activityIndicator = uie.createIndicatorWindow(L('uploading'));
                // The activity indicator must be added to a window or view for it to appear
                // self.add(activityIndicator);

                //activityIndicator.add(ind);
                //activityIndicator.openIndicator();
                // ind.show();

                // handle EXIF data
                // TODO: what other EXIF goodies should we extract?
                if ( typeof (event.metadata) !== 'undefined') {
                    // new Alert(L('alert'),null,event.metadata);
                    try {

                        if (saveLB === '1') {// users can opt-out of saving LB data for posts
                            self.longitude = event.metadata.location.longitude;
                            self.latitude = event.metadata.location.latitude;
                        } else {
                            self.longitude = 0;
                            self.latitude = 0;
                        }

                        if ( typeof (event.metadata.cropRect) !== 'undefined') {
                            self.contentW = event.metadata.cropRect.width;
                            self.contentH = event.metadata.cropRect.height;
                        } else if ( typeof (event.metadata.exif) !== 'undefined') {

                            self.contentW = event.metadata.exif.PixelXDimension;
                            self.contentH = event.metadata.exif.PixelYDimension;

                        }
                    } catch(x) {
                        ;
                    }
                    var content = event.media;

                    var contentURL = event.metadata.path;

                    // SOMETHING IN THIS SECTION CAUSES IOS CRASH WITH LARGE / LOCAL IMAGE UPLOADS
                    if (userimage === null) {

                        var mtp = event.mediaType;

                        if (contentURL.toLowerCase().indexOf('jpg') > -1)
                            mtp = 'image/jpeg';
                        else if (contentURL.toLowerCase().indexOf('png') > -1)
                            mtp = 'image/png';
                        else if (contentURL.toLowerCase().indexOf('jpeg') > -1)
                            mtp = 'image/jpeg';
                        else if (contentURL.toLowerCase().indexOf('bmp') > -1)
                            mtp = 'image/bmp';
                        else if (contentURL.toLowerCase().indexOf('gif') > -1)
                            mtp = 'image/gif';
                        else if (contentURL.toLowerCase().indexOf('m4a') > -1)
                            mtp = 'audio/mp4';
                        else if (contentURL.toLowerCase().indexOf('mov') > -1)
                            mtp = 'video/quicktime';

/* TODO: fix this ... for now it is not */
                        var assetslibrary = require('ti.assetslibrary');

                        var asset = assetslibrary.getAsset(contentURL, function(e) {
                            var detailRow = function(name, val) {
                                var row = Ti.UI.createTableViewRow({
                                    height : 44
                                });
                                var title = Ti.UI.createLabel({
                                    left : 10,
                                    width : 150,
                                    font : {
                                        fontSize : 16,
                                        fontWeight : 'bold'
                                    },
                                    textAlign : 'left',
                                    color : 'black',
                                    text : name
                                });

                                var value = Ti.UI.createLabel({
                                    right : 10,
                                    width : 150,
                                    font : {
                                        fontSize : 14
                                    },
                                    textAlign : 'right',
                                    color : '#aaa',
                                    text : val
                                });
                                row.add(title);
                                row.add(value);
                                return row;
                            };

                            var assetOrientation = function(asset) {
                                switch (asset.orientation) {
                                case assetslibrary.AssetOrientationUp:
                                    return 'UP';
                                case assetslibrary.AssetOrientationDown:
                                    return 'DOWN';
                                case assetslibrary.AssetOrientationLeft:
                                    return 'LEFT';
                                case assetslibrary.AssetOrientationRight:
                                    return 'RIGHT';
                                case assetslibrary.AssetOrientationUpMirrored:
                                    return 'UP-mirrored';
                                case assetslibrary.AssetOrientationDownMirrored:
                                    return 'DOWN-mirrored';
                                case assetslibrary.AssetOrientationLeftMirrored:
                                    return 'LEFT-mirrored';
                                case assetslibrary.AssetOrientationRightMirrored:
                                    return 'RIGHT-mirrored';
                                default:
                                    return 'undefined';
                                }
                            };

                            var Window = require('ui/common/ApplicationWindow');

                            var detailswin = new Window(L('fileDetails'), true);

                            var detailsTable = Ti.UI.createTableView({
                                top : 40,
                                separatorColor : '#eee'
                            });
                            detailswin.add(detailsTable);

                            var sections = [];
                            var section1 = Ti.UI.createTableViewSection();
                            section1.headerTitle = 'general info';

                            section1.add(detailRow('type:', e.asset.type));
                            section1.add(detailRow('urls:', JSON.stringify(e.asset.URLs)));

                        // setting lowres has no effect...
                            var img = Ti.UI.createImageView({
                                left : 5,
                                // height : 40,
                                // width : 40,
                                image : e.asset.thumbnail,
                                hires : false

                            });
                            Ti.API.info('asset urls:' + e.asset.URLs);
                            section1.add(detailRow('duration:', e.asset.duration));
                            section1.add(detailRow('orientation:', assetOrientation(e.asset)));
                            // section1.add(detailRow('date:', e.asset.date.shortString(true)));
                            if (e.asset.location) {
                                section1.add(detailRow('location:', e.asset.location.latitude + ', ' + e.asset.location.longitude));
                            }
                            sections.push(section1);
                            //for every e.asset we can have several representations
                            var representations = e.asset.representations;
                            if (representations) {
                                for (var i = 0; i < representations.length; i++) {
                                    var rep = representations[i];
                                    var section = Ti.UI.createTableViewSection();
                                    section.headerTitle = 'representation: ' + rep.UTI;
                                    section.add(detailRow('url:', rep.url));
                                    section.add(detailRow('filename:', rep.filename));
                                    section.add(detailRow('orientation', assetOrientation(rep)));
                                    section.add(detailRow('size', rep.size));
                                    section.add(detailRow('scale', rep.scale));
                                    section.add(detailRow('metadata', JSON.stringify(rep.metadata)));
                                    Ti.API.info(JSON.stringify(rep.metadata));
                                    sections.push(section);
                                }
                            }
                            detailsTable.data = sections;

                            // contentURL = JSON.stringify(e.asset.URLs);
                            var ContentViewer = require('ui/common/ApplicationContentViewer');
                            var contentImage = img;
                            contentImage.setWidth(Ti.UI.FILL);
                            contentImage.setTop(60);
                            contentImage.setOpacity(.8);
                            contentImage.setLeft(0);
                            contentImage.setZIndex(-1);

                            contentImage.addEventListener('longpress', function(e) {
                                detailswin.open({
                                    animated : true
                                });

                            });

                            self.add(contentImage);
                            contentImage.show();

                            // self.contentURL = contentURL;

                            description.show();

                        }, function(e) {
                            new Alert(L('alert'), null, L('errorUploadingData'));
                            var ErrorDialog = require('ui/common/ErrorDialog');
                            var err = new ErrorDialog(e);
                        });


                       description.show();
                       description.focus();
                    }
                }

                var XhrX = require('ui/common/util/Xhr');
                /* success callback fired after media retrieved from gallery */
                var xhrT = new XhrX({

                    onload : function(e) {
                        try {
                            var itemData = JSON.parse(this.responseText);
                            self.contentURL = itemData.filename;
                            // self.ind.hide();
                            if (userimage !== null) {
                                var ContentViewer = require('ui/common/ApplicationContentViewer');
                                var contentImage = new ContentViewer(self.contentURL, itemData.mimeType, Ti.App.CurrentUser.id, null, Ti.App.IMAGE_NAME_STD);

                                userimage.setImage(contentImage._url);

                                self.close();
                            } else {
                                // self.postButton.show();
                                //var ContentViewer = require('ui/common/ApplicationContentViewer');
                                //var contentImage = new ContentViewer(self.contentURL, itemData.mimeType, Ti.App.CurrentUser.id, null, Ti.App.IMAGE_NAME_STD);
                            }
                        } catch(e) {
                            var ErrorDialog = require('ui/common/ErrorDialog');
                            var err = new ErrorDialog(e);
                        }

                        activityIndicator.closeIndicator();
                        // reset upload progress indicator
                        ind.value = 0;
                        ind.hide();
                    },
                    oncancel : function(e) {
                        activityIndicator.closeIndicator();
                        ind.hide();
                        self.close();
                    },
                    onerror : function(e) {
                        activityIndicator.closeIndicator();
                        ind.hide();
                        var ErrorDialog = require('ui/common/ErrorDialog');
                        var err = new ErrorDialog(e);
                        // err.open();
                    },
                    onsendstream : function(e) {
                        ind.value = e.progress;
                        Ti.API.info('uploadFromGallery ONSENDSTREAM - PROGRESS: ' + e.progress);
                        if (ind.value > 99) {
                            ind.activityLabel.setValue(L('convertingImages'));
                        }
                    },
                    timeout : 190000 /* in milliseconds */

                });

                // onsendstream called repeatedly, use the progress property to

                var connurl = Ti.App.REST_API_BASE_URL + 'content/-1/upload';
                xhrT.setRequestHeader("Content-Type", "multipart/form-data");
                xhrT.setRequestHeader("Set-Cookie", "JSESSIONID=" + Ti.App.CurrentSessionID);

                userphoto = (userimage != null);

                xhrT.open('PUT', connurl);
                xhrT.send({
                    'binary_content' : content, /* event.media holds blob from gallery */
                    'userImage' : userphoto
                });

            },
            cancel : function(e) {
                self.close();
            }
        });

        /* Create a progress bar */
        var ind = Ti.UI.createProgressBar({
            width : Ti.UI.FILL,
            height : 10,
            min : 0,
            max : 1,
            value : 0,
            //   style : Ti.UI.iPhone.ProgressBarStyle.BAR,
            top : 70,
            message : L('uploadingContent'),
            font : {
                fontSize : 10,
            },
        });
        self.ind = ind;
        self.add(ind);
        ind.show();
        //

    },

    /**
     * post contents of articles
     *
     * @param {Object} description
     * @param {Object} self
     */
    submitContent : function(description, self, userphoto) {

        self.userphoto = userphoto;

        if (userphoto) {
            this.createContentRecord(self);
        } else {
            // show the category picker now...
            description.blur();
            try {
                self._label.setText(L('uploadInProgressPleaseSetRatingAndCategory'));
                self._picker.setVisible(true);
            } catch(e) {
                new Alert(L('alert'), null, L('errorSubmittingContent'));
            }
        }

    },

    /**
     * submit
     */
    createContentRecord : function(self) {
        var XhrX = require('ui/common/util/Xhr');
        var xhrB = new XhrX({
            onload : function(e) {

                self.postButton.hide();
                self._picker.hide();
                self.hide();
                var itemData = JSON.parse(this.responseText);
                // load content detail
                if (!self.userphoto) {
                    var ContentDetail = require('ui/common/ContentDetail');
                    Ti.App.cd = new ContentDetail(itemData.id, itemData, true);
                    Ti.App.Tab1.open(Ti.App.cd, {
                        animated : true,
                        //transition : slideOut
                    });

                    Ti.App.cd.addEventListener('open', function(e) {
                        self.close();
                    });
                } else {
                    self.hide();
                    self.close();
                }
            },
            onerror : function(e) {
                self._picker.hide();
                var ErrorDialog = require('ui/common/ErrorDialog');
                var err = new ErrorDialog(e);
                // err.open();
            },
            timeout : 25000 /* in milliseconds */
        });
        var connurl = Ti.App.REST_API_BASE_URL + 'content/-1/create';
        xhrB.setRequestHeader("Content-Type", "multipart/form-data");
        xhrB.setRequestHeader("Set-Cookie", "JSESSIONID=" + Ti.App.CurrentSessionID);

        if (!self.userphoto) {
            var cat1 = self._picker.getSelectedRow(0);
            var cat2 = self._picker.getSelectedRow(1);
        } else {
            var cat1 = -1;
            var cat2 = -1;
        }

        xhrB.open('PUT', connurl);
        xhrB.send({
            'author' : Ti.App.CurrentUser.id,
            'device' : Ti.App.platform,
            'copyright' : 'CC Public Domain or Else',
            'url' : self.contentURL,
            'longitude' : self.longitude,
            'latitude' : self.latitude,
            'width' : self.width,
            'height' : self.height,
            'categories' : JSON.stringify([cat1.id, cat2.id]),
            'mimeType' : this.getMimeType(self.contentURL),
            'description' : self._description.getValue(),
        });
    },

    /**
     * handy
     *
     * @param {Object} fn
     */
    getMimeType : function(fn) {

        filename = fn.toLowerCase();

        if (filename.indexOf('.txt') > -1) {
            return 'text/plain';
        }
        if (filename.indexOf('.mov') > -1) {
            return 'video/quicktime';
        }
        if (filename.indexOf('.png') > -1) {
            return 'image/png';
        }

        if ((filename.indexOf('.jpeg') > -1) || (filename.indexOf('.jpg') > -1)) {
            return 'image/jpeg';
        }
        if ((filename.indexOf('.mp4') > -1) || (filename.indexOf('.m4a') > -1)) {
            return 'audio/mp';
        }
        if (filename.indexOf('.html') > -1) {
            return 'text/html';
        }

        return 'text/html';
    }
};
module.exports = AssetUploader;
