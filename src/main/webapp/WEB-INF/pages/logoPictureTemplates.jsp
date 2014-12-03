<div id="prototypeTemplate" style="display: none;">
    <div style="float: left;" class="prototype">
        <div class="logoName">
            <div id="name" style="float: left;"></div>
            <div id="upload" class="link" pictureId="" requiredHeight="" requiredWidth="" entityId="" entityType="">upload</div>
            <input type="hidden" id="code"/>
            <input type="file" id="uploadPrototype" style="display: none;">
        </div>
        <div id="block">
            <img src="" class="image" manuallyAdded="">
        </div>
        <div id="size"></div>
    </div>
</div>
<div id="personTemplate" style="display: none;">
    <div class="onePersonBlock">
        <input type="hidden" id="personId" value="">
        <input type="hidden" id="personPictureId" value="">
        <div style="float: left; width: 100%; padding-left: 10px; line-height: 30px; margin-bottom: 10px; font-size: 14px;">
            <div style="float: left; margin-right: 10px;" id="fullName"></div>
            <div style="float: left;"><a id="linkedIn" class="link" target="_blank">LinkedIn Page</a></div>
        </div>
        <div class="logoOneBlock">
            <div style="float: left; width: 100%;">
                <div id="dragAndDropContainer" style="float: left; width: 125px; padding-left: 10px;"></div>
                <div id="cropContainer" style="float: left; width: 770px;" class="cropContainer"></div>
            </div>
            <div style="float: left; width: 100%; margin-top: 10px;">
                <div style="float: left; width: 770px; margin-left: 145px;" id="prototypesPerson"></div>
            </div>
        </div>
        <div class="done">
            <input type="button" value="Save" id="save" class="buttonSave" style="float: right; margin-top: 0;" personId="">
            <div class="checkboxNotFound">
                <label for="logoNotFound">Logo for this person not found</label>
                <input type="checkbox" id="logoNotFound">
            </div>
        </div>
    </div>
</div>