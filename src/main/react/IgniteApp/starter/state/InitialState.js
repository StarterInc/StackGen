'use strict';

module.exports = {

  // including the SYSTEM built in categories and any that you want to add here...
  categories:{
    builtIn:[
      {id:6, name:"damage levels", description:"Categories describing damage amount", parent_id:-1},
      {id:39, name:"Industrial", description:"Industrial", parent_id:-1},
      {id:67, name:"Masonry: Stone", description:"Brick", parent_id:-1},
      {id:17, name:"Fire", description:"Arson", parent_id:-1},
      {id:119, name:"Biological (pests", description:"fungus)", parent_id:-1},
      {id:36, name:"Public use", description:"e.g.", parent_id:-1},
    ],
    testers:[
      {id:1, name:"artifact", description:"Any type of physical artifact.", parent_id:0},
      {id:2, name:"person", description:"A human being.", parent_id:0},
      {id:3, name:"structure", description:"A physical building or other built structure.", parent_id:0},
      {id:4, name:"manuscript", description:"A written artifact", parent_id:0},
    ],
    damageTypes:[
      {id:5, name:"damage types", description:"Categories describing damage", parent_id:-1}, // parent category
      {id:20, name:"Geo Technical", description:"Geo Technical", parent_id:5},
      {id:21, name:"Mechanical", description:"Mechanical", parent_id:5},
      {id:22, name:"Non Structural", description:"Non Structural", parent_id:5},
      {id:23, name:"Structural", description:"Structural", parent_id:5},
    ],
    hazardsNatural:[
      {id:7, name:"natural hazard types", description:"Types of hazards to persons and objects from natural causes.", parent_id:-1},
      {id:9, name:"Earthquake", description:"Possibility or result of Earthquake", parent_id:7},
      {id:10, name:"Storm", description:"Risk from storm", parent_id:7},
      {id:11, name:"Fire", description:"Risk from fire", parent_id:7},
      {id:12, name:"Flood", description:"Risk from flooding events", parent_id:7},
      {id:13, name:"Water level", description:"Risk of water damage from rising seas", parent_id:7},
      {id:14, name:"Corrosion / rust", description:"Damage from oxidation and other corrosion", parent_id:7},
      {id:15, name:"Landslide", description:"Damage from unstable or collapsing land", parent_id:7},
    ],
    hazardsHuman:[
      {id:8, name:"man made hazard types", description:"Types of hazards to persons and objects from human causes.", parent_id:-1},
      {id:16, name:"Warfare", description:"Military Activity", parent_id:8},
      {id:18, name:"Human activity/ non military", description:"non military human activity", parent_id:8},
      {id:19, name:"Pollution", description:"industrial hazards", parent_id:8},
    ],
    damageSeverity:[
      {id:24, name:"Overall Condition", description:"Overall Condition", parent_id:-1},
      {id:25, name:"No damage/ Good condition", description:"No damage/ Good condition", parent_id:24},
      {id:26, name:"Minor Damage/ Fair condition", description:"Minor Damage/ Fair condition", parent_id:24},
      {id:27, name:"Moderate Damage/ Poor condition", description:"Moderate Damage/ Poor condition", parent_id:24},
      {id:28, name:"Severe Damage/ Very bad condition", description:"Severe Damage/ Very bad condition", parent_id:24},
      {id:29, name:"Collapsed/Destroyed", description:"Collapsed/Destroyed", parent_id:24},
    ],
    buildingUse:[
      {id:30, name:"Building Use", description:"Building Use", parent_id:-1},
      {id:31, name:"Archive", description:"Archive", parent_id:30},
      {id:32, name:"Commercial", description:"Commercial", parent_id:30},
      {id:33, name:"Library", description:"Library", parent_id:30},
      {id:34, name:"Monument/Site", description:"Monument/Site", parent_id:30},
      {id:35, name:"Museum", description:"Museum", parent_id:30},
      {id:37, name:"Religious", description:"Religious", parent_id:30},
      {id:38, name:"Residential", description:"Residential", parent_id:30},
    ],
    buldingOwnership:[
      {id:40, name:"Building Ownership / Control", description:"Building Ownership / Control", parent_id:-1},
      {id:41, name:"Public (Government)", description:"Public (Government)", parent_id:40},
      {id:42, name:"Private", description:"Private", parent_id:40},
      {id:43, name:"Unknown", description:"Unknown", parent_id:40},
      {id:44, name:"Religious authority", description:"Religious authority", parent_id:40},
    ],
    levelOfProtection:[
      {id:45, name:"Level of the protection", description:"Level of the protection", parent_id:-1},
      {id:46, name:"International", description:"International", parent_id:45},
      {id:47, name:"National", description:"National", parent_id:45},
      {id:48, name:"Local", description:"Local", parent_id:45},
      {id:49, name:"Unprotected", description:"Unprotected", parent_id:45},
      {id:50, name:"UNESCO World Heritage Site", description:"UNESCO World Heritage Site", parent_id:45},
    ],
    architecturalStyle:[
      {id:51, name:"Architectural Style", description:"Architectural Style", parent_id:-1},
      {id:52, name:"Vernacular/Traditional", description:"Vernacular/Traditional", parent_id:51},
      {id:53, name:"Contemporary", description:"Contemporary", parent_id:51},
      {id:54, name:"Indigenous", description:"Indigenous", parent_id:51},
      {id:55, name:"Ancient", description:"Ancient", parent_id:51},
    ],
    hazardsSafety:[
      {id:56, name:"Personal safety hazards", description:"Personal safety hazards", parent_id:-1},
      {id:57, name:"None", description:"None", parent_id:56},
      {id:58, name:"Imminent Structural Collapse/Falling Material", description:"Imminent Structural Collapse/Falling Material", parent_id:56},
      {id:59, name:"Electrical", description:"Electrical", parent_id:56},
      {id:60, name:"Chemical", description:"Chemical", parent_id:56},
      {id:61, name:"Unexploded Ordinance/ Landmines", description:"Unexploded Ordinance/ Landmines", parent_id:56},
      {id:62, name:"Other", description:"Other", parent_id:56},
    ],
    siteComponents:[
      {id:64, name:"Total floors of building", description:"Total floors of building", parent_id:63},
      {id:66, name:"Roof Type", description:"Roof Type", parent_id:63},
      {id:72, name:"Dome", description:"Dome", parent_id:63},
      {id:73, name:"Thatch", description:"Thatch", parent_id:63},
      {id:74, name:"Flat Roof", description:"Flat Roof", parent_id:63},
      {id:75, name:"Sloping Roof", description:"Sloping Roof", parent_id:63},
      {id:76, name:"Vaulted Roof", description:"Vaulted Roof", parent_id:63},
      {id:77, name:"Other", description:"Other", parent_id:63},
    ],
    constructionComponents:[
      {id:63, name:"Building / Site - Components", description:"Building / Site - Components", parent_id:-1},
      {id:65, name:"Type of Construction", description:"Type of Construction", parent_id:63},

      // TODO: support N levels (?)
        {id:68, name:"Wood/ Timber frame", description:"Wood/ Timber frame", parent_id:65},
        {id:69, name:"Concrete", description:"Concrete", parent_id:65},
        {id:70, name:"Iron/Steel frame", description:"Iron/Steel frame", parent_id:65},
        {id:71, name:"Other", description:"Other", parent_id:65},
    ],

    objectTypes:[
      {id:78, name:"Types of collection / objects", description:"Types of objects", parent_id:-1},
      {id:79, name:"Audio Tape", description:"Audio Tape", parent_id:78},
      {id:80, name:"Book", description:"Book", parent_id:78},
      {id:81, name:"Canvas", description:"Canvas", parent_id:78},
      {id:82, name:"Coins", description:"Coins", parent_id:78},
      {id:83, name:"Document", description:"Document", parent_id:78},
      {id:84, name:"DVD/CD", description:"DVD/CD", parent_id:78},
      {id:85, name:"Manuscript", description:"Manuscript", parent_id:78},
      {id:86, name:"Mosaics", description:"Mosaics", parent_id:78},
      {id:87, name:"Painting", description:"Painting", parent_id:78},
      {id:88, name:"Pottery", description:"Pottery", parent_id:78},
      {id:89, name:"Scroll", description:"Scroll", parent_id:78},
      {id:90, name:"Statue", description:"Statue", parent_id:78},
      {id:91, name:"Furniture", description:"Furniture", parent_id:78},
    ],

    objectMaterialsOrganic:[
      {id:92, name:"Organic Materials", description:"Organic Materials", parent_id:-1},
      {id:93, name:"Feather", description:"Feather", parent_id:92},
      {id:94, name:"Ivory / Bone ", description:"Ivory / Bone ", parent_id:92},
      {id:95, name:"Leather", description:"Leather", parent_id:92},
      {id:96, name:"Paper", description:"Paper", parent_id:92},
      {id:97, name:"Papyrus", description:"Papyrus", parent_id:92},
      {id:98, name:"Plant material", description:"Plant material", parent_id:92},
      {id:99, name:"Plastic", description:"Plastic", parent_id:92},
      {id:100, name:"Shell", description:"Shell", parent_id:92},
      {id:101, name:"Textile", description:"Textile", parent_id:92},
      {id:102, name:"Wood", description:"Wood", parent_id:92},
    ],

    objectMaterialsInorganic:[
      {id:104, name:"Ceramics", description:"Ceramics", parent_id:103},
      {id:105, name:"Fired Pottery", description:"Fired Pottery", parent_id:103},
      {id:106, name:"Glass", description:"Glass", parent_id:103},
      {id:107, name:"Metal- Copper Based", description:"Metal- Copper Based", parent_id:103},
      {id:108, name:"Metal- Gold/Silver Based", description:"Metal- Gold/Silver Based", parent_id:103},
      {id:109, name:"Metal- Iron Based", description:"Metal- Iron Based", parent_id:103},
      {id:110, name:"Stone", description:"Stone", parent_id:103},
      {id:111, name:"Sun-Baked Clay", description:"Sun-Baked Clay", parent_id:103},
    ],

    estimatedCollectionObjects:[
      {id:'112', name:"Estimated Number of Objects", description:"Estimated Number of Objects", parent_id:-1},
      {id:113, name:"Less than 100", description:"Less than 100", parent_id:112},
      {id:114, name:"Around 100", description:"Around 100", parent_id:112},
      {id:115, name:"Between 100-500", description:"Between 100-500", parent_id:112},
      {id:116, name:"Greater than 500", description:"Greater than 500", parent_id:112},
      {id:117, name:"Everything is in a pile", description:"Everything is in a pile", parent_id:112},
    ],

    damageSources:[
      {id:118, name:"Possible sources of Damage", description:"Possible sources of Damage", parent_id:-1},
      {id:120, name:"Collapse of the building", description:"Collapse of the building", parent_id:118},
      {id:121, name:"Extreme Physical force- Bombardment", description:"Extreme Physical force- Bombardment", parent_id:118},
      {id:122, name:"Extreme Physical force- Earthquake", description:"Extreme Physical force- Earthquake", parent_id:118},
      {id:123, name:"Fire", description:"Fire", parent_id:118},
      {id:124, name:"Vandalism", description:"Vandalism", parent_id:118},
      {id:125, name:"Water", description:"Water", parent_id:118},
      {id:126, name:"Water & Contaminants", description:"Water & Contaminants", parent_id:118},
      {id:127, name:"Water & Mud", description:"Water & Mud", parent_id:118},
    ],

    damageTypesObject:[
      {id:128, name:"Type of object damage", description:"Type of object damage", parent_id:-1},
      {id:141, name:"Broken", description:"Broken", parent_id:128},
      {id:142, name:"Burned", description:"Burned", parent_id:128},
      {id:143, name:"Chemical Deposits on Surface", description:"Chemical Deposits on Surface", parent_id:128},
      {id:144, name:"Chipping", description:"Chipping", parent_id:128},
      {id:145, name:"Cracks", description:"Cracks", parent_id:128},
      {id:146, name:"Deformed", description:"Deformed", parent_id:128},
      {id:147, name:"Folds", description:"Folds", parent_id:128},
      {id:148, name:"Loose or flaking paint layers", description:"Loose or flaking paint layers", parent_id:128},
      {id:149, name:"Loss of Material", description:"Loss of Material", parent_id:128},
      {id:150, name:"Soiled/ Grimy", description:"Soiled/ Grimy", parent_id:128},
      {id:151, name:"Soot Deposits on Surface", description:"Soot Deposits on Surface", parent_id:128},
      {id:152, name:"Tears", description:"Tears", parent_id:128},
    ],

    damageLevelPercentage:[
      {id:153, name:"Overall percent of damage", description:"Overall percent of damage", parent_id:-1},
      {id:154, name:"Major- Everything or >50% broken and/or wet and/or contaminated or/and burned", description:"Major- Everything or >50% broken and/or wet and/or contaminated or/and burned", parent_id:153},
      {id:155, name:"Moderate- Less than half or 25-50% of the objects or/and partially wet and/or partially contaminated", description:"Moderate- Less than half or 25-50% of the objects or/and partially wet and/or partially contaminated and/or partially burned", parent_id:153},
      {id:156, name:"Minor- One fraction or < 20% of objects are broken and/orwer and/or contaminated and/ or burned", description:"Minor- One fraction or < 20% of objects are broken and/orwer and/or contaminated and/ or burned", parent_id:153},
      {id:157, name:"Intact", description:"Intact", parent_id:153},
    ],

    objectInOriginalLocation:[
      {id:158, name:"Object in pre-event location", description:"Object in pre-event location", parent_id:-1},
      {id:159, name:"Yes", description:"Yes", parent_id:158},
      {id:160, name:"No", description:"No", parent_id:158},
      {id:161, name:"Not sure", description:"Not sure", parent_id:158},
    ],

    objectLocations:[
      {id:162, name:"Location of objects", description:"Location of objects", parent_id:-1},
      {id:163, name:"Outdoors", description:"Outdoors", parent_id:162},
      {id:164, name:"Indoors", description:"Indoors", parent_id:162},
    ],

    roomPriorFunction:[
      {id:165, name:"Room Function Before", description:"Function of room before event", parent_id:-1},
      {id:166, name:"Display", description:"Display", parent_id:165},
      {id:167, name:"Storage", description:"Storage", parent_id:165},
      {id:168, name:"Other", description:"Other", parent_id:165},
      {id:169, name:"Don\t know", description:"Don\t Know", parent_id:165},
    ],

    previousRecordsExist:[
      {id:170, name:"Previous Records", description:"Previous Records Exist?", parent_id:-1},
      {id:171, name:"Yes", description:"Yes", parent_id:170},
      {id:172, name:"No", description:"No", parent_id:170},
      {id:173, name:"Not sure", description:"Not sure", parent_id:170},
    ],

    documentation:[
      {id:174, name:"Documentation", description:"Does the previous documentation indicate the significance of the damaged objects?", parent_id:-1},
      {id:175, name:"No", description:"No", parent_id:174},
      {id:176, name:"Aesthetic", description:"Aesthetic", parent_id:174},
      {id:177, name:"Historic", description:"Historic", parent_id:174},
      {id:178, name:"Economic", description:"Economic", parent_id:174},
      {id:179, name:"Cultural", description:"Cultural", parent_id:174},
      {id:180, name:"Religious", description:"Religious", parent_id:174},
      {id:181, name:"Scientific", description:"Scientific", parent_id:174},
      {id:182, name:"Not sure", description:"Not sure", parent_id:174}
    ]
  },
  assessments:[
    {
      id:'-1',
      owner:'none',
      thumbnail: require('../assets/static_web/img-placeholder@2x.png'),
      name: 'New Assessment',
      damage: 'none',
      startDate: 'TODAY | 0 days',
      endDate: '30 Jun 2016',

      description: '0 sites, 0 buildings, 0 collections',
      region: {
        description: 'Current Location',
        latitude: 30.4197283,
        longitude: -9.6477148,

        latitudeDelta: 0.03,
        longitudeDelta: 0.03,

        sites:[{
          id:-1,
          name:'New Site',
          coordinates: [{
              latitude: 36.1989496,
              longitude: 44.206389
            },{
              latitude: 36.1989496+ 0.0922,
              longitude: 44.206389 + 0.03
            },{
              latitude: 36.1989496+ 0.03,
              longitude: 44.206389 - 0.03
            },{
              latitude: 36.1989496- 0.03,
              longitude: 44.206389 - 0.03
            }
          ],
          damage:[
            {
              id:-1,
              type:'none',
              amount: 'no detected damage (0%)',
            }
          ],
          objects:[
            {
              id:'-1',
              type: 'new object',
              description: 'describe this object',
              damage:{
                type:'none',
                category: -1,
                percentage: 0,
                description:'detailed damage description'
              },
              media:[
                {
                  id:'-1',
                  description:'describe image',
                  thumbnail: require( '../assets/img-placeholder@2x.png'),
                  src: require('../assets/static_web/img-placeholder@2x.png'),
                  mimeType:'image/png',
                },
              ]
            }
          ]

          }]
      }
    },{
      id:'a1',
      owner: '1',
      thumbnail: require( '../assets/img-placeholder@2x.png'),
      name: 'Aleppo, SYRIA',
      damage: 'War, Armed Conflict',
      startDate: '14 April 2016',
      endDate: '30 April 2016',
      description: 'Month-long Aleppo assessment including  team from UNESCO and GHF.',
      eventDescription:'Combat in the northern part of the City',
      region: {
        description: 'Town Square - Aleppo, SYRIA',
        latitude:   36.1989496,
        longitude:  37.1618491,

        latitudeDelta: .03,
        longitudeDelta: .03,

        sites:[{
          id:100,
          name:'Mosque Haddadin',

          objects:[
            {
              id:'obj-323',
              type: 'book',
              description: 'ancient copy of koran',
              damage:{
                type:'water',
                category: 3,
                percentage: 40,
                description:'pipe burst in the library. water damage to cover and pages'
              },
              media:[
                {
                  id:'333',
                  description:'back cover',
                  thumbnail: require( '../assets/img-placeholder@2x.png'),
                  src: require('../assets/static_web/img-placeholder@2x.png'),
                  mimeType:'image/png',
                },
                {
                  id:'334',
                  description: 'a closeup of the Koran',
                  thumbnail: require( '../assets/img-placeholder@2x.png'),
                  src: require('../assets/static_web/img-placeholder@2x.png'),
                  mimeType:'image/png',
                },
              ]
            },
            {
              id:'obj-324',
              type: 'statue',
              description: 'small crystal goblet',
              damage:{
                type:'cracks',
                category: 1,
                percentage: 20,
                description:'sustained cracking after falling from shelf'
              },
              media:[
                {
                  id:'330',
                  description:'underside cover',
                  thumbnail: require( '../assets/img-placeholder@2x.png'),
                  src: require('../assets/static_web/img-placeholder@2x.png'),
                  mimeType:'image/png',
                },
                {
                  id:'320',
                  description: 'a top of goblet',
                  thumbnail: require( '../assets/img-placeholder@2x.png'),
                  src: require('../assets/static_web/img-placeholder@2x.png'),
                  mimeType:'image/png',
                },
              ]
            },
            {
              id:'obj-325',
              type: 'sword',
              description: 'steel scimitar',
              damage:{
                type:'rust',
                category: 1,
                percentage: 20,
                description:'rusty blade'
              }
            }
          ],
          coordinates: [{
              latitude: 36.1989496,
              longitude: 44.206389
            },{
              latitude: 36.1989496 + 0.0922,
              longitude: 44.206389 + 0.03
            },{
              latitude: 36.1989496 + 0.03,
              longitude: 44.206389 - 0.03
            },{
              latitude: 36.1989496 - 0.03,
              longitude: 44.206389 - 0.03
            }
          ],
          damage:[
            {
              id:100,
              type:'structural',
              amount: 'collapsed / destroyed (90-100%)',
            },
            {
              id:101,
              type:'structural',
              amount: 'severe (60-90%)',
            },
            {
              id:102,
              type:'structural',
              amount: 'severe (60-90%)',
            },
          ],
          media:[
            {
              id:'333',
              description:'back cover',
              thumbnail: require( '../assets/img-placeholder@2x.png'),
              src: require('../assets/static_web/img-placeholder@2x.png'),
              mimeType:'image/png',
            },
            {
              id:'334',
              description: 'a closeup of the Koran',
              thumbnail: require( '../assets/img-placeholder@2x.png'),
              src: require('../assets/static_web/img-placeholder@2x.png'),
              mimeType:'image/png',
            },
          ]

          },{
            id:101,
            name:'Madrasa Sultaniya',
            coordinates: [{
              latitude: 36.1989496,
              longitude: 44.206389
            },{
              latitude: 36.1989496+ 0.0922,
              longitude: 44.206389 + 0.03
            },{
              latitude: 36.1989496+ 0.03,
              longitude: 44.206389 - 0.03
            },{
              latitude: 36.1989496- 0.03,
              longitude: 44.206389 - 0.03
            }
          ],
          damage:[
              {
                id:103,
                type:'structural',
                amount: 'collapsed / destroyed (90-100%)',
              },
              {
                id:104,
                type:'structural',
                amount: 'severe (60-90%)',
              },
              {
                id:105,
                type:'structural',
                amount: 'severe (60-90%)',
              },
            ],
            media:[
              {
                id:'333',
                description:'back cover',
                thumbnail: require( '../assets/img-placeholder@2x.png'),
                src: require('../assets/static_web/img-placeholder@2x.png'),
                mimeType:'image/png',
              },
              {
                id:'334',
                description: 'a closeup of the Koran',
                thumbnail: require( '../assets/img-placeholder@2x.png'),
                src: require('../assets/static_web/img-placeholder@2x.png'),
                mimeType:'image/png',
              },
            ]
          }
        ],
        coordinates: [
          {
            latitude: 36.1989496,
            longitude: 44.206389
          },{
            latitude: 36.1989496+ 0.0922,
            longitude: 44.206389 + 0.03
          },{
            latitude: 36.1989496+ 0.03,
            longitude: 44.206389 - 0.03
          },{
            latitude: 36.1989496- 0.03,
            longitude: 44.206389 - 0.03
          }
        ]
      }
    },{
      id:'a3',
      owner:'1',
      thumbnail: require('../assets/static_web/img-placeholder@2x.png'),
      name: 'Mosul, Nineveh',
      damage: 'War, Armed Conflict',
      startDate: '1 Mar 2016 | 6 days',
      endDate: '3 April 2016',
      description: '1 site, 2 buildings, 5 collections',
      region: {
        description: 'Abadir, Morrocco',
        latitude: 36.3528778,
        longitude: 43.070611,

        latitudeDelta: 0.03,
        longitudeDelta: 0.03,

        sites:[{
          id:100,
          name:'Mosque Haddadin',
          coordinates: [{
              latitude: 36.1989496,
              longitude: 44.206389
            },{
              latitude: 36.1989496+ 0.0922,
              longitude: 44.206389 + 0.03
            },{
              latitude: 36.1989496+ 0.03,
              longitude: 44.206389 - 0.03
            },{
              latitude: 36.1989496- 0.03,
              longitude: 44.206389 - 0.03
            }
          ],
          damage:[
            {
              id:100,
              type:'structural',
              amount: 'collapsed / destroyed (90-100%)',
            },
            {
              id:101,
              type:'structural',
              amount: 'severe (60-90%)',
            },
            {
              id:102,
              type:'structural',
              amount: 'severe (60-90%)',
            },
          ],

          }
        ]
      }
    }
  ],

 roles:[
   {
     id:'-1',
     name:'New Team',
     description:'Add description',
     read_only:false,
     users:[
       {
         id:'1',
         name:'joe',
         email:'abc123@google.com',
         avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
         roles: [
           {id:22,name:'BigDecimal'},
           {id:33, name:'Starter'},
           {id:44,name:'Syria'}
         ]
       },{
         id:'2',
         name:'jane',
         avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
         email:'abc123@google.com',
         roles: [
           {id:22,name:'BigDecimal'},
           {id:33, name:'Starter'},
           {id:44,name:'Syria'}
         ]
       },{
         id:'3',
         name:'kBigDecimal',
         avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
         email:'kBigDecimal23@gmail.com',
         roles: [
           {id:22,name:'BigDecimal'},
           {id:33, name:'Starter'},
           {id:44,name:'Syria'}
         ]
       },
     ],
   },{
     id:'1',
     name:'GHF',
     description:'Team created specifically for GHF',
     read_only:false,
     users:[
        {
          id:'1',
          name:'joe',
          email:'abc123@google.com',
          avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
          roles: [
            {id:22,name:'BigDecimal'},
            {id:33, name:'Starter'},
            {id:44,name:'Syria'}
          ]
        },{
          id:'2',
          name:'jane',
          avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
          email:'abc123@google.com',
          roles: [
            {id:22,name:'BigDecimal'},
            {id:33, name:'Starter'},
            {id:44,name:'Syria'}
          ]
        },{
          id:'3',
          name:'kBigDecimal',
          avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
          email:'kBigDecimal23@gmail.com',
          roles: [
            {id:22,name:'BigDecimal'},
            {id:33, name:'Starter'},
            {id:44,name:'Syria'}
          ]
        },
      ],
      assessments:[
        {id:100},
        {id:200},
        {id:300}
      ]
   },{
     id:'2',
     name:'Team Egypt',
     description:'Egypt team',
     read_only:false,
     users:[
        {
          id:'1',
          name:'joe',
          avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
          email:'abc123@google.com',
          roles: [
            {id:22,name:'BigDecimal'},
            {id:33, name:'Starter'},
            {id:44,name:'Syria'}
          ]
        }
      ],
      assessments:[
        {
          id:'a2',
          owner:'Shared',
          thumbnail: require('../assets/static_web/img-placeholder@2x.png'),
          name: 'Sana, Yemen',
          damage: 'Fire',
          startDate: '2 April 2016 | 3 days',
          endDate: '30 April 2016',

          description: '1 site, 3 buildings, 1 collection',

          region: {

            description: 'South Yemen',
            latitude: 15.348333,
            longitude:  44.206389,

            latitudeDelta: .03,
            longitudeDelta: .03,

            sites:[{
              id:100,
              name:'Mosque Sathad',

              coordinates: [ {
                latitude: 15.348333,
                longitude: 44.206389
              },{
                latitude: 15.348333 + 0.0922,
                longitude: 44.206389 + 0.03
              },{
                latitude: 15.348333 + 0.03,
                longitude: 44.206389 - 0.03
              },{
                latitude: 15.348333 - .03,
                longitude: 44.206389 - .03
              }],
              damage:[
                {
                  id:100,
                  type:'structural',
                  amount: 'cracked / damaged (30-70%)',
                },
                {
                  id:101,
                  type:'structural',
                  amount: 'severe (60-90%)',
                },
                {
                  id:102,
                  type:'structural',
                  amount: 'severe (60-90%)',
                },
              ],

              }
            ]
          }

        }
      ]
   },{
     id:'3',
     name:'Team Alcatraz',
     description:'Team created specifically for Alcatraz assessment',
     read_only:false,
     users:[
        {
          id:'2',
          name:'jane',
          avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
          email:'abc123@google.com',
          roles: [
            {id:22,name:'BigDecimal'},
            {id:33, name:'Starter'},
            {id:44,name:'Syria'}
          ]
        }
      ],
      assessments:[
        {
          id:'a4',
          owner:'Shared',
          thumbnail: require('../assets/static_web/img-placeholder@2x.png'),
          name: 'Agadir, Morocco',
          damage: 'Earthquake',
          startDate: '24 March 2015 | 4 days',
          endDate: '30 Jun 2016',

          description: '2 sites, 7 buildings, 2 collections',
          region: {
            description: 'Abadir, Morrocco',
            latitude: 30.4197283,
            longitude: -9.6477148,

            latitudeDelta: 0.03,
            longitudeDelta: 0.03,

            sites:[{
              id:100,
              name:'Mosque Haddadin',
              coordinates: [{
                  latitude: 36.1989496,
                  longitude: 44.206389
                },{
                  latitude: 36.1989496+ 0.0922,
                  longitude: 44.206389 + 0.03
                },{
                  latitude: 36.1989496+ 0.03,
                  longitude: 44.206389 - 0.03
                },{
                  latitude: 36.1989496- 0.03,
                  longitude: 44.206389 - 0.03
                }
              ],
              damage:[
                {
                  id:100,
                  type:'structural',
                  amount: 'collapsed / destroyed (90-100%)',
                },
                {
                  id:101,
                  type:'structural',
                  amount: 'severe (60-90%)',
                },
                {
                  id:102,
                  type:'structural',
                  amount: 'severe (60-90%)',
                },
              ],

              }
            ]
          }
      }
      ]
   }

 ],
 appStatus:{
   debug: true,
   entries: [
     {loggedIn:false},
     {connected:false},
     {wifi:false},
     {threeG:true},
     {fourG:false},
     {LTE:false},
     {ok:true},
  ]
 },

  currentAssessment:{
      region: {
        currentAssessmentId: -1,
        longitude:-122.031,
        latitude:37.332,
        markers: [],
        polygons: [],
        editing: null,
      },
  },

 userInfo:{
   username: null,
   password :null,
   created:-1,
   deviceId:'-1',
   timestamp:null,
   StarterPro:false,
   isadmin:false,
   avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
   userLevel: null,
   lastUpdated: 234234222,
   // lastAssessment: '',
   preferences: [],
   roles: [],
   badges: [],
   devices: [],
   locations: [],
   stats: []
 },

  userInfoMocked:{
    created: 234234234,
    deviceId:'234234',
    timestamp: 234234234234,
    username: 'carboload',
    StarterPro: false,
    isadmin: false,
    password:'chukles',
    avatarImage: require( '../assets/static_web/img-placeholder@2x.png'),
    userLevel: 'na',
    lastUpdated: 234234222,
    // lastAssessment: '',
    preferences: [],
    roles: [],
    badges: [],
    devices: [],
    locations: [],
    stats: []
  },
  fetching:false,
};
